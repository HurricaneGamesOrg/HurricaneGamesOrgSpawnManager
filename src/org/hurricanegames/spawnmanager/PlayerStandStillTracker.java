package org.hurricanegames.spawnmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.hurricanegames.commandlib.utils.Tuple;

public class PlayerStandStillTracker implements Listener {

	private static final PlayerStandStillTracker instance = new PlayerStandStillTracker();

	public static PlayerStandStillTracker getInstance() {
		return instance;
	}

	private boolean init;
	public void start() {
		if (init) {
			throw new IllegalStateException("Already initialized");
		}
		init = true;

		Bukkit.getPluginManager().registerEvents(this, SpawnManagerPlugin.getInstance());
	}

	protected final Map<UUID, Tuple<Consumer<TrackingResult>, BukkitTask>> tracked = new HashMap<>();

	public boolean isTracking(Player player) {
		return tracked.containsKey(player.getUniqueId());
	}

	public void startTracking(Player player, int timeoutTicks, Consumer<TrackingResult> result) {
		UUID uuid = player.getUniqueId();
		if (timeoutTicks < 0) {
			throw new IllegalArgumentException("Timeout ticks can't be negative");
		}
		if (tracked.containsKey(uuid)) {
			throw new IllegalStateException("Player " + player.getUniqueId() + " is already tracked");
		}
		BukkitTask task = Bukkit.getScheduler().runTaskLater(SpawnManagerPlugin.getInstance(), () -> tryFinishTrack(uuid, false, TrackingResult.NO_MOVEMENT), timeoutTicks);
		tracked.put(uuid, new Tuple<>(result, task));
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!event.getFrom().toVector().equals(event.getTo().toVector())) {
			tryFinishTrack(event.getPlayer().getUniqueId(), true, TrackingResult.MOVEMENT);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		tryFinishTrack(event.getPlayer().getUniqueId(), true, TrackingResult.OFFLINE);
	}

	protected void tryFinishTrack(UUID uuid, boolean cancelTask, TrackingResult result) {
		Tuple<Consumer<TrackingResult>, BukkitTask> tracker = tracked.remove(uuid);
		if (tracker != null) {
			if (cancelTask) {
				tracker.getObject2().cancel();
			}
			tracker.getObject1().accept(result);
		}
	}

	public enum TrackingResult {
		NO_MOVEMENT,
		MOVEMENT,
		OFFLINE
	}

}
