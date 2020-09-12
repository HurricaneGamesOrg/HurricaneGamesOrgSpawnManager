package org.hurricanegames.spawnmanager;

import org.bukkit.plugin.java.JavaPlugin;
import org.hurricanegames.commandlib.commands.BukkitCommandExecutor;
import org.hurricanegames.spawnmanager.commands.SpawnCommandHelper;
import org.hurricanegames.spawnmanager.commands.admin.SpawnForceTeleportCommand;
import org.hurricanegames.spawnmanager.commands.admin.SpawnSetLocationCommand;
import org.hurricanegames.spawnmanager.commands.game.SpawnTeleportCommand;

public class SpawnManagerPlugin extends JavaPlugin {

	private final SpawnContainer container = new SpawnContainer(this);

	private boolean init;

	@Override
	public void onEnable() {
		container.getConfig().reload();
		container.getLocalization().reload();
		container.init();
		container.load();

		SpawnCommandHelper commandhelper = new SpawnCommandHelper(container);
		getCommand("spawn").setExecutor(new BukkitCommandExecutor(new SpawnTeleportCommand(commandhelper)));
		getCommand("fspawn").setExecutor(new BukkitCommandExecutor(new SpawnForceTeleportCommand(commandhelper), SpawnManagerPermissions.ADMIN));
		getCommand("setspawn").setExecutor(new BukkitCommandExecutor(new SpawnSetLocationCommand(commandhelper), SpawnManagerPermissions.ADMIN));

		init = true;
	}

	@Override
	public void onDisable() {
		if (!init) {
			return;
		}

		container.save();
	}

}
