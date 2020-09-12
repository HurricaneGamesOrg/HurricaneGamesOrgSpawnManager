package org.hurricanegames.spawnmanager;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnPlayerListener implements Listener {

	protected final SpawnContainer container;

	public SpawnPlayerListener(SpawnContainer container) {
		this.container = container;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerSpawnLocationEvent(PlayerSpawnLocationEvent event) {
		if (event.getPlayer().hasPlayedBefore()) {
			return;
		}
		Location location = container.getLocation();
		if (location != null) {
			event.setSpawnLocation(location);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (event.isBedSpawn() || event.isAnchorSpawn()) {
			return;
		}
		Location location = container.getLocation();
		if (location != null) {
			event.setRespawnLocation(location);
		}
	}

}
