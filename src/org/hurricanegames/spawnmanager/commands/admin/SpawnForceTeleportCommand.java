package org.hurricanegames.spawnmanager.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.spawnmanager.commands.SpawnCommandHelper;

public class SpawnForceTeleportCommand extends CommandBasic<SpawnCommandHelper> {

	public SpawnForceTeleportCommand(SpawnCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentOnlinePlayer.class) Player otherPlayer
	) {
		if (helper.getContainer().forceTeleport(otherPlayer)) {
			throw new CommandResponseException(ChatColor.GREEN + "Игрок {0} телепортирован на спавн", otherPlayer.getName());
		} else {
			throw new CommandResponseException(ChatColor.RED + "Игрок {0} не телепортирован на спавн", otherPlayer.getName());
		}
	}

	@Override
	protected String getHelpExplainMessage() {
		return "телепортирует указанного игрока на спавн";
	}

}
