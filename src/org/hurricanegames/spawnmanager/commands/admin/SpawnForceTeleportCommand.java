package org.hurricanegames.spawnmanager.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.spawnmanager.commands.SpawnManagerCommandHelper;

public class SpawnForceTeleportCommand extends CommandBasic<SpawnManagerCommandHelper> {

	public SpawnForceTeleportCommand(SpawnManagerCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentOnlinePlayer.class) Player otherPlayer
	) {
		if (helper.getContainer().forceTeleport(otherPlayer)) {
			throw new CommandResponseException(ChatColor.GREEN + "Player {0} teleported to spawn", otherPlayer.getName());
		} else {
			throw new CommandResponseException(ChatColor.RED + "Player {0} not teleported to spawn", otherPlayer.getName());
		}
	}

	@Override
	protected String getHelpExplainMessage() {
		return "forcefully teleport player to spawn";
	}

}
