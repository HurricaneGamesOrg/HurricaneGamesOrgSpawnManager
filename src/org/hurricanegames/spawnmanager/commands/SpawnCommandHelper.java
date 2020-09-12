package org.hurricanegames.spawnmanager.commands;

import org.hurricanegames.commandlib.commands.CommandHelper;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfoProvider;
import org.hurricanegames.spawnmanager.SpawnContainer;
import org.hurricanegames.spawnmanager.SpawnManagerLocalization;

public class SpawnCommandHelper extends CommandHelper<SpawnManagerLocalization, BukkitPlayerInfo, BukkitPlayerInfoProvider> {

	protected final SpawnContainer container;

	public SpawnCommandHelper(SpawnContainer container) {
		super(container.getLocalization(), BukkitPlayerInfoProvider.INSTANCE);
		this.container = container;
	}

	public SpawnContainer getContainer() {
		return container;
	}

}
