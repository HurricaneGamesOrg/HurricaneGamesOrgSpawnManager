package org.hurricanegames.spawnmanager;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;
import org.hurricanegames.commandlib.commands.BukkitCommandExecutor;
import org.hurricanegames.spawnmanager.commands.SpawnCommandHelper;
import org.hurricanegames.spawnmanager.commands.admin.SpawnForceTeleportCommand;
import org.hurricanegames.spawnmanager.commands.admin.SpawnSetLocationCommand;
import org.hurricanegames.spawnmanager.commands.game.SpawnTeleportCommand;

public class SpawnManagerPlugin extends JavaPlugin {

	private static SpawnManagerPlugin instance;

	public static SpawnManagerPlugin getInstance() {
		return instance;
	}

	public static File getDataFile(String... childs) {
		File file = getInstance().getDataFolder();
		for (String child : childs) {
			file = new File(child);
		}
		return file;
	}

	public SpawnManagerPlugin() {
		instance = this;
	}

	private final SpawnContainer container = new SpawnContainer();

	private boolean init;

	@Override
	public void onEnable() {
		PlayerStandStillTracker.getInstance().start();
		SpawnManagerConfig.getInstance().reload();
		SpawnManagerLocalization.getInstance().reload();
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
