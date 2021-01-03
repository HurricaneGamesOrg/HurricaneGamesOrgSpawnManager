package org.hurricanegames.spawnmanager;

import java.io.File;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.hurricanegames.pluginlib.configurations.ConfigurationUtils;
import org.hurricanegames.pluginlib.configurations.builtin.DefaultCommandMessages;

public class SpawnManagerLocalization extends DefaultCommandMessages {

	protected final File storageFile;

	public SpawnManagerLocalization(File storageFile) {
		this.storageFile = storageFile;
	}

	@Override
	public void load() {
		load(YamlConfiguration.loadConfiguration(storageFile));
	}

	@Override
	public void save() {
		YamlConfiguration config = new YamlConfiguration();
		save(config);
		ConfigurationUtils.safeSave(config, storageFile);
	}

	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	public String TIMEDIFFFORMAT_DAYS = " days ";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	public String TIMEDIFFFORMAT_HOURS = " hours ";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	public String TIMEDIFFFORMAT_MINUTES = " minutes ";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	public String TIMEDIFFFORMAT_SECONDS = " seconds";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	public String TIMEDIFFFORMAT_ZERO = "less than a second";

	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	public String TELEPORT_START = ChatColor.GREEN + "You are being teleported, dont move for {0}";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	public String TELEPORT_SUCCESS = ChatColor.GREEN + "You have been teleported to spawn";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	public String TELEPORT_ERROR_NOSPAWN = ChatColor.RED + "Teleportation impossible because spawn point is unvailable";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	public String TELEPORT_ERROR_TRACKED = ChatColor.RED + "You are already being teleported";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	public String TELEPORT_ERROR_MOVED = ChatColor.RED + "Teleportation has been cancelled because you moved";
	@ConfigurationFieldDefinition(fieldType = SimpleColorizedStringConfigurationField.class)
	public String TELEPORT_ERROR_UNKNOWN = ChatColor.RED + "Teleportation failed because of unknown reason";

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
