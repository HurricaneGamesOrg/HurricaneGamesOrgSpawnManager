package org.hurricanegames.spawnmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.hurricanegames.pluginlib.utils.types.Tuple;

public class PlayerStandStillTracker implements Listener {

	protected final SpawnManagerPlugin plugin;

	public PlayerStandStillTracker(SpawnManagerPlugin plugin) {
		this.plugin = plugin;
	}

	protected final Map<UUID, Tuple<Consumer<TrackingResult>, BukkitTask>> tracked = new HashMap<>();

	public boolean isTracking(Player player) {
		return tracked.containsKey(player.getUniqueId());
	}

	public void startTracking(Player player, long timeoutTicks, Consumer<TrackingResult> result) {
		UUID uuid = player.getUniqueId();
		if (timeoutTicks < 0) {
			throw new IllegalArgumentException("Timeout ticks can't be negative");
		}
		if (tracked.containsKey(uuid)) {
			throw new IllegalStateException("Player " + player.getUniqueId() + " is already tracked");
		}
		BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin, () -> tryFinishTrack(uuid, false, TrackingResult.NO_MOVEMENT), timeoutTicks);
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
