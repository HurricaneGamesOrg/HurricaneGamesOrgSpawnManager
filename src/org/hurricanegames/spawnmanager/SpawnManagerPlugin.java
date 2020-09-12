package org.hurricanegames.spawnmanager;

import org.bukkit.plugin.java.JavaPlugin;
import org.hurricanegames.commandlib.commands.BukkitCommandExecutor;
import org.hurricanegames.spawnmanager.commands.SpawnManagerCommandHelper;
import org.hurricanegames.spawnmanager.commands.admin.SpawnForceTeleportCommand;
import org.hurricanegames.spawnmanager.commands.admin.SpawnManagerAdminCommands;
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

		SpawnManagerCommandHelper commandhelper = new SpawnManagerCommandHelper(container);
		getCommand("spawnmanageradmin").setExecutor(new BukkitCommandExecutor(new SpawnManagerAdminCommands(commandhelper), SpawnManagerPermissions.ADMIN));
		getCommand("setspawn").setExecutor(new BukkitCommandExecutor(new SpawnSetLocationCommand(commandhelper), SpawnManagerPermissions.ADMIN));
		getCommand("fspawn").setExecutor(new BukkitCommandExecutor(new SpawnForceTeleportCommand(commandhelper), SpawnManagerPermissions.ADMIN));
		getCommand("spawn").setExecutor(new BukkitCommandExecutor(new SpawnTeleportCommand(commandhelper)));

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
