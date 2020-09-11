package org.hurricanegames.spawnmanager;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitRunnable;
import org.hurricanegames.commandlib.configurations.ConfigurationUtils;

import spawnapi.SpawnAPI;

public class SpawnContainer {

	private boolean init;
	public void init() {
		if (init) {
			throw new IllegalStateException("Already initialized");
		}
		init = true;

		Bukkit.getPluginManager().registerEvents(new SpawnPlayerListener(this), SpawnManagerPlugin.getInstance());
		try {
			Bukkit.getServicesManager().register(SpawnAPI.class, new SpawnAPIImpl(this), SpawnManagerPlugin.getInstance(), ServicePriority.Normal);
			SpawnManagerPlugin.getInstance().getLogger().log(Level.INFO, "Enabled SpawnAPI integration");
		} catch (Throwable t) {
			SpawnManagerPlugin.getInstance().getLogger().log(Level.INFO, "Failed to enable SpawnAPI integration: " + t.getMessage());
		}
	}

	protected String worldName;
	protected Location spawnLocation;

	public void setLocation(Location location) {
		Location previousLocation = getLocation();
		if (previousLocation != null) {
			previousLocation.getChunk().removePluginChunkTicket(SpawnManagerPlugin.getInstance());
		}

		if (location != null) {
			location.getWorld().setSpawnLocation(location);

			location.getChunk().addPluginChunkTicket(SpawnManagerPlugin.getInstance());

			worldName = location.getWorld().getName();
			spawnLocation = location.clone();
			spawnLocation.setWorld(null);
		} else {
			worldName = null;
			spawnLocation = null;
		}
	}

	public Location getLocation() {
		if (worldName == null) {
			return null;
		}
		World world = Bukkit.getWorld(worldName);
		if (world == null) {
			return null;
		}
		Location loc = spawnLocation.clone();
		loc.setWorld(world);
		return loc;
	}

	public TeleportAttemptResult teleport(Player player, Consumer<Boolean> result) {
		Consumer<Boolean> nonnullResult = result != null ? result : success -> {};

		SpawnManagerLocalization localization = SpawnManagerLocalization.getInstance();

		Location location = getLocation();
		if (location != null) {
			int delayS = SpawnManagerConfig.getInstance().TELEPORT_DELAY.intValue();
			if (player.hasPermission(SpawnManagerPermissions.TELEPORT_NODELAY) || (delayS <= 0)) {
				boolean teleportSuccess = player.teleport(player);
				if (teleportSuccess) {
					player.sendMessage(localization.TELEPORT_SUCCESS);
					return TeleportAttemptResult.SUCCESS;
				} else {
					player.sendMessage(localization.TELEPORT_ERROR_UNKNOWN);
					return TeleportAttemptResult.FAIL;
				}
			}

			PlayerStandStillTracker tracker = PlayerStandStillTracker.getInstance();
			if (tracker.isTracking(player)) {
				player.sendMessage(localization.TELEPORT_ERROR_TRACKED);
				return TeleportAttemptResult.FAIL;
			} else {
				tracker.startTracking(player, delayS * 20, trackResult -> {
					switch (trackResult) {
						case OFFLINE: {
							nonnullResult.accept(Boolean.FALSE);
							break;
						}
						case MOVEMENT: {
							player.sendMessage(localization.TELEPORT_ERROR_MOVED);
							nonnullResult.accept(Boolean.FALSE);
							break;
						}
						case NO_MOVEMENT: {
							boolean teleportSuccess = player.teleport(player);
							if (teleportSuccess) {
								player.sendMessage(localization.TELEPORT_SUCCESS);
								nonnullResult.accept(Boolean.TRUE);
							} else {
								player.sendMessage(localization.TELEPORT_ERROR_UNKNOWN);
								nonnullResult.accept(Boolean.FALSE);
							}
							break;
						}
					}
				});
				player.sendMessage(localization.formatTeleportStartMessage(TimeUnit.SECONDS.toMillis(delayS)));
				return TeleportAttemptResult.DELAYED;
			}
		} else {
			player.sendMessage(localization.TELEPORT_ERROR_NOSPAWN);
			return TeleportAttemptResult.FAIL;
		}
	}

	public enum TeleportAttemptResult {
		SUCCESS, DELAYED, FAIL
	}

	public boolean forceTeleport(Player player) {
		Location location = getLocation();
		if (location != null) {
			return player.teleport(location);
		}
		return false;
	}


	protected static final String world_name_path = "world";
	protected static final String location_path = "location";

	protected File getFile() {
		return new File(SpawnManagerPlugin.getInstance().getDataFolder(), "spawn.yml");
	}

	public void load() {
		ConfigurationSection config = YamlConfiguration.loadConfiguration(getFile());

		worldName = config.getString(world_name_path);
		if (worldName == null) {
			return;
		}

		spawnLocation = config.getLocation(location_path);

		//keep spawn chunk loaded as soon as the world it is in loads
		new BukkitRunnable() {
			@Override
			public void run() {
				Location location = getLocation();
				if (location != null) {
					cancel();
					location.getChunk().addPluginChunkTicket(SpawnManagerPlugin.getInstance());
				}
			}
		}.runTaskTimer(SpawnManagerPlugin.getInstance(), 1, 1);
	}

	public void save() {
		YamlConfiguration config = new YamlConfiguration();

		if (spawnLocation != null) {
			config.set(world_name_path, worldName);
			config.set(location_path, spawnLocation);
		}

		ConfigurationUtils.safeSave(config, getFile());
	}

}
