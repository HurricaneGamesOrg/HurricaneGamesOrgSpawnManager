package org.hurricanegames.spawnmanager.commands.admin;

import org.hurricanegames.commandlib.commands.CommandRouter;
import org.hurricanegames.commandlib.providers.commands.SimpleConfigurationReloadCommand;
import org.hurricanegames.spawnmanager.commands.SpawnManagerCommandHelper;

public class SpawnManagerAdminCommands extends CommandRouter<SpawnManagerCommandHelper> {

	public SpawnManagerAdminCommands(SpawnManagerCommandHelper helper) {
		super(helper);
		addCommand("reloadcfg", new SimpleConfigurationReloadCommand<>(helper, helper.getContainer().getConfig(), () -> "main"));
		addCommand("reloadi10n", new SimpleConfigurationReloadCommand<>(helper, helper.getContainer().getLocalization(), () -> "i10n"));
	}

}
