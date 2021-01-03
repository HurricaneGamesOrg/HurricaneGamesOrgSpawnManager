package org.hurricanegames.spawnmanager.commands.game;

import org.bukkit.entity.Player;
import org.hurricanegames.pluginlib.commands.CommandBasic;
import org.hurricanegames.spawnmanager.commands.SpawnManagerCommandHelper;

public class SpawnTeleportCommand extends CommandBasic<SpawnManagerCommandHelper> {

	public SpawnTeleportCommand(SpawnManagerCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentSenderPlayer.class) Player senderPlayer
	) {
		helper.getContainer().teleport(senderPlayer, null);
	}

	@Override
	protected String getHelpExplainMessage() {
		return "teleports to spawn";
	}

}
