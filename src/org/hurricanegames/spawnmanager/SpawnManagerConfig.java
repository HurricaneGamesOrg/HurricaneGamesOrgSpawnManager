package org.hurricanegames.spawnmanager;

import java.io.File;

import org.hurricanegames.pluginlib.configurations.annotated.AnnotatedRootYamlConfiguration;

public class SpawnManagerConfig extends AnnotatedRootYamlConfiguration {

	public SpawnManagerConfig(File storageFile) {
		super(storageFile);
	}

	@ConfigurationFieldDefinition(fieldType = LongConfigurationField.class)
	public Long TELEPORT_DELAY = Long.valueOf(5);

}
