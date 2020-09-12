package org.hurricanegames.spawnmanager.commands.game;

import org.bukkit.entity.Player;
import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.spawnmanager.commands.SpawnCommandHelper;

public class SpawnTeleportCommand extends CommandBasic<SpawnCommandHelper> {

	public SpawnTeleportCommand(SpawnCommandHelper helper) {
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
