package org.hurricanegames.spawnmanager;

import java.io.File;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.hurricanegames.commandlib.providers.messages.DefaultMessages;

public class SpawnManagerLocalization extends DefaultMessages {

	private static final SpawnManagerLocalization instance = new SpawnManagerLocalization();

	public static SpawnManagerLocalization getInstance() {
		return instance;
	}

	@Override
	protected File getStorageFile() {
		return new File(SpawnManagerPlugin.getInstance().getDataFolder(), "localization.yml");
	}

	@ConfigurationFieldDefinition
	public String TIMEDIFFFORMAT_DAYS = " дней ";
	@ConfigurationFieldDefinition
	public String TIMEDIFFFORMAT_HOURS = " часов ";
	@ConfigurationFieldDefinition
	public String TIMEDIFFFORMAT_MINUTES = " минут ";
	@ConfigurationFieldDefinition
	public String TIMEDIFFFORMAT_SECONDS = " секунд";
	@ConfigurationFieldDefinition
	public String TIMEDIFFFORMAT_ZERO = "меньше секунды";

	@ConfigurationFieldDefinition
	public String TELEPORT_START = ChatColor.GREEN + "Вы ожидаете телепортацию, не двигайтесь в течение {0}";
	@ConfigurationFieldDefinition
	public String TELEPORT_SUCCESS = ChatColor.GREEN + "Вы телепортировались на спавн";
	@ConfigurationFieldDefinition
	public String TELEPORT_ERROR_UNKNOWN = ChatColor.RED + "Телепортация не удалась по неизвестной причине";
	@ConfigurationFieldDefinition
	public String TELEPORT_ERROR_NOSPAWN = ChatColor.RED + "Телепортация не удалась так как точка спавна недоступна";
	@ConfigurationFieldDefinition
	public String TELEPORT_ERROR_MOVED = ChatColor.RED + "Телепортация не удалась так как вы двинулись во время ожидания";
	@ConfigurationFieldDefinition
	public String TELEPORT_ERROR_TRACKED = ChatColor.RED + "Вы уже телепортируетесь";

	public String formatTimeDiff(long diffMS) {
		StringBuilder message = new StringBuilder();
		long days = TimeUnit.MILLISECONDS.toDays(diffMS);
		if (days != 0) {
			message.append(days);
			message.append(TIMEDIFFFORMAT_DAYS);
		}
		long hours = TimeUnit.MILLISECONDS.toHours(diffMS) % TimeUnit.DAYS.toHours(1);
		if (hours != 0) {
			message.append(hours);
			message.append(TIMEDIFFFORMAT_HOURS);
		}
		long minutes = TimeUnit.MILLISECONDS.toMinutes(diffMS) % TimeUnit.HOURS.toMinutes(1);
		if (minutes != 0) {
			message.append(minutes);
			message.append(TIMEDIFFFORMAT_MINUTES);
		}
		long seconds = TimeUnit.MILLISECONDS.toSeconds(diffMS) % TimeUnit.MINUTES.toSeconds(1);
		if (seconds != 0) {
			message.append(seconds);
			message.append(TIMEDIFFFORMAT_SECONDS);
		}
		if (message.length() == 0) {
			message.append(TIMEDIFFFORMAT_ZERO);
		}
		return message.toString();
	}

	public String formatTeleportStartMessage(long timeMS) {
		return MessageFormat.format(TELEPORT_START, formatTimeDiff(timeMS));
	}

}
