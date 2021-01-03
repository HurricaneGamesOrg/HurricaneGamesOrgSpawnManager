package org.hurricanegames.spawnmanager.commands.admin;

import org.hurricanegames.pluginlib.commands.CommandRouter;
import org.hurricanegames.pluginlib.commands.builtin.ListPluginPermissionsCommand;
import org.hurricanegames.pluginlib.commands.builtin.RootConfigurationReloadCommand;
import org.hurricanegames.spawnmanager.SpawnManagerPermissions;
import org.hurricanegames.spawnmanager.commands.SpawnManagerCommandHelper;

public class SpawnManagerAdminCommands extends CommandRouter<SpawnManagerCommandHelper> {

	public SpawnManagerAdminCommands(SpawnManagerCommandHelper helper) {
		super(helper);
		setPermission(SpawnManagerPermissions.ADMIN);
		addCommand("permissions", new ListPluginPermissionsCommand<>(helper));
		addCommand("reloadcfg", new RootConfigurationReloadCommand<>(helper, helper.getContainer().getConfig(), () -> "main"));
		addCommand("reloadi10n", new RootConfigurationReloadCommand<>(helper, helper.getContainer().getLocalization(), () -> "i10n"));
	}

}
