package org.hurricanegames.spawnmanager;

import java.io.File;

import org.hurricanegames.commandlib.configurations.ConfigurationUtils.LongConfigurationField;
import org.hurricanegames.commandlib.configurations.SimpleConfiguration;

public class SpawnManagerConfig extends SimpleConfiguration {

	protected final File storageFile;

	public SpawnManagerConfig(File storageFile) {
		this.storageFile = storageFile;
	}

	@Override
	protected File getStorageFile() {
		return storageFile;
	}

	@ConfigurationFieldDefinition(fieldType = LongConfigurationField.class)
	public Long TELEPORT_DELAY = Long.valueOf(5);

}
