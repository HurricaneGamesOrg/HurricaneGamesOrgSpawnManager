package org.hurricanegames.spawnmanager.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.spawnmanager.commands.SpawnCommandHelper;

public class SpawnSetLocationCommand extends CommandBasic<SpawnCommandHelper> {

	public SpawnSetLocationCommand(SpawnCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentSenderPlayer.class) Player senderPlayer
	) {
		helper.getContainer().setLocation(senderPlayer.getLocation());
		helper.getContainer().save();
		throw new CommandResponseException(ChatColor.GREEN + "Точка спавна установлена");
	}

	@Override
	protected String getHelpExplainMessage() {
		return "устанавливает позицию первого спавна для новых игроков";
	}

}
