package org.hurricanegames.spawnmanager;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;

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

	protected final SpawnManagerPlugin plugin;
	protected final SpawnManagerConfig config;
	protected final SpawnManagerLocalization localization;
	protected final PlayerStandStillTracker standStillTracker;

	public SpawnContainer(SpawnManagerPlugin plugin) {
		this.plugin = plugin;
		this.config = new SpawnManagerConfig(new File(plugin.getDataFolder(), "config.yml"));
		this.localization = new SpawnManagerLocalization(new File(plugin.getDataFolder(), "localization.yml"));
		this.standStillTracker = new PlayerStandStillTracker(plugin);
	}

	public SpawnManagerPlugin getPlugin() {
		return plugin;
	}

	public SpawnManagerConfig getConfig() {
		return config;
	}

	public SpawnManagerLocalization getLocalization() {
		return localization;
	}


	private boolean init;
	public void init() {
		if (init) {
			throw new IllegalStateException("Already initialized");
		}
		init = true;

		plugin.getServer().getPluginManager().registerEvents(new SpawnPlayerListener(this), plugin);
		plugin.getServer().getPluginManager().registerEvents(standStillTracker, plugin);
		try {
			plugin.getServer().getServicesManager().register(SpawnAPI.class, new SpawnAPIImpl(this), plugin, ServicePriority.Normal);
			plugin.getLogger().log(Level.INFO, "Enabled SpawnAPI integration");
		} catch (Throwable t) {
			plugin.getLogger().log(Level.INFO, "Failed to enable SpawnAPI integration: " + t.getMessage());
		}
	}

	protected String worldName;
	protected Location spawnLocation;

	public void setLocation(Location location) {
		Location previousLocation = getLocation();
		if (previousLocation != null) {
			previousLocation.getChunk().removePluginChunkTicket(plugin);
		}

		if (location != null) {
			location.getWorld().setSpawnLocation(location);

			location.getChunk().addPluginChunkTicket(plugin);

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
		World world = plugin.getServer().getWorld(worldName);
		if (world == null) {
			return null;
		}
		Location loc = spawnLocation.clone();
		loc.setWorld(world);
		return loc;
	}

	public TeleportAttemptResult teleport(Player player, Consumer<Boolean> result) {
		Consumer<Boolean> nonnullResult = result != null ? result : success -> {};

		Location location = getLocation();
		if (location != null) {
			long delayS = config.TELEPORT_DELAY.longValue();
			if (player.hasPermission(SpawnManagerPermissions.TELEPORT_NODELAY) || (delayS <= 0)) {
				boolean teleportSuccess = player.teleport(location);
				if (teleportSuccess) {
					player.sendMessage(localization.TELEPORT_SUCCESS);
					return TeleportAttemptResult.SUCCESS;
				} else {
					player.sendMessage(localization.TELEPORT_ERROR_UNKNOWN);
					return TeleportAttemptResult.FAIL;
				}
			}

			if (standStillTracker.isTracking(player)) {
				player.sendMessage(localization.TELEPORT_ERROR_TRACKED);
				return TeleportAttemptResult.FAIL;
			} else {
				standStillTracker.startTracking(player, delayS * 20, trackResult -> {
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
							boolean teleportSuccess = player.teleport(location);
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

	protected File getStorageFile() {
		return new File(plugin.getDataFolder(), "spawn.yml");
	}

	public void load() {
		ConfigurationSection storageConfig = YamlConfiguration.loadConfiguration(getStorageFile());

		worldName = storageConfig.getString(world_name_path);
		if (worldName == null) {
			return;
		}

		spawnLocation = storageConfig.getLocation(location_path);

		//keep spawn chunk loaded as soon as the world it is in loads
		new BukkitRunnable() {
			@Override
			public void run() {
				Location location = getLocation();
				if (location != null) {
					cancel();
					location.getChunk().addPluginChunkTicket(plugin);
				}
			}
		}.runTaskTimer(plugin, 1, 1);
	}

	public void save() {
		YamlConfiguration storageConfig = new YamlConfiguration();

		if (spawnLocation != null) {
			storageConfig.set(world_name_path, worldName);
			storageConfig.set(location_path, spawnLocation);
		}

		ConfigurationUtils.safeSave(storageConfig, getStorageFile());
	}

}
