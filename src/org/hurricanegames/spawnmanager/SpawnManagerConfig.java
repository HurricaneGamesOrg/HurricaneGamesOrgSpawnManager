package org.hurricanegames.spawnmanager;

import java.io.File;

import org.hurricanegames.commandlib.configurations.ConfigurationUtils.LongConfigurationField;
import org.hurricanegames.commandlib.configurations.SimpleConfiguration;

public class SpawnManagerConfig extends SimpleConfiguration {

	private static final SpawnManagerConfig instance = new SpawnManagerConfig();

	public static SpawnManagerConfig getInstance() {
		return instance;
	}

	@Override
	protected File getStorageFile() {
		return SpawnManagerPlugin.getDataFile("config.yml");
	}

	@ConfigurationFieldDefinition(fieldType = LongConfigurationField.class)
	public Long TELEPORT_DELAY = Long.valueOf(5);

}
