package org.hurricanegames.spawnmanager.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.hurricanegames.pluginlib.commands.CommandBasic;
import org.hurricanegames.pluginlib.commands.CommandResponseException;
import org.hurricanegames.spawnmanager.SpawnManagerPermissions;
import org.hurricanegames.spawnmanager.commands.SpawnManagerCommandHelper;

public class SpawnSetLocationCommand extends CommandBasic<SpawnManagerCommandHelper> {

	public SpawnSetLocationCommand(SpawnManagerCommandHelper helper) {
		super(helper, SpawnManagerPermissions.ADMIN);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentSenderPlayer.class) Player senderPlayer
	) {
		helper.getContainer().setLocation(senderPlayer.getLocation());
		helper.getContainer().save();
		throw new CommandResponseException(ChatColor.GREEN + "Spawn point set");
	}

	@Override
	protected String getHelpExplainMessage() {
		return "Sets spawn point for players without defined spawn location";
	}

}
