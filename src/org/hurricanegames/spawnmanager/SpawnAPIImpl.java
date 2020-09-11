package org.hurricanegames.spawnmanager;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import spawnapi.SpawnAPI;

public class SpawnAPIImpl implements SpawnAPI {

	private final SpawnContainer container;

	protected SpawnAPIImpl(SpawnContainer container) {
		this.container = container;
	}

	@Override
	public Location getLocation() {
		return container.getLocation();
	}

	@Override
	public void setLocation(Location location) {
		container.setLocation(location);
	}

	@Override
	public TeleportAttemptResult teleport(Player player, Consumer<Boolean> result) {
		switch (container.teleport(player, result)) {
			case SUCCESS: {
				return TeleportAttemptResult.SUCCESS;
			}
			case DELAYED: {
				return TeleportAttemptResult.DELAYED;
			}
			case FAIL: {
				return TeleportAttemptResult.FAIL;
			}
		}
		return TeleportAttemptResult.FAIL;
	}

	@Override
	public boolean forceTeleport(Player player) {
		return container.forceTeleport(player);
	}

}
