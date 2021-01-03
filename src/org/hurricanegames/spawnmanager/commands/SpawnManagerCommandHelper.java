package org.hurricanegames.spawnmanager.commands;

import org.hurricanegames.pluginlib.commands.CommandHelper;
import org.hurricanegames.pluginlib.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.pluginlib.playerinfo.BukkitPlayerInfoProvider;
import org.hurricanegames.spawnmanager.SpawnContainer;
import org.hurricanegames.spawnmanager.SpawnManagerLocalization;
import org.hurricanegames.spawnmanager.SpawnManagerPlugin;

public class SpawnManagerCommandHelper extends CommandHelper<SpawnManagerPlugin, SpawnManagerLocalization, BukkitPlayerInfo, BukkitPlayerInfoProvider> {

	protected final SpawnContainer container;

	public SpawnManagerCommandHelper(SpawnContainer container) {
		super(container.getPlugin(), container.getLocalization(), BukkitPlayerInfoProvider.INSTANCE);
		this.container = container;
	}

	public SpawnContainer getContainer() {
		return container;
	}

}
