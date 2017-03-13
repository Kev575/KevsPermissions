package kev575.permissions.io;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
	
	private final FileConfiguration config;
	private final Runnable save;
	
	/*
	 * Initialize the manager<br>
	 * Might only work for plugins/KevsPermissions/config.yml
	 */
	public ConfigManager(FileConfiguration config, Runnable save) {
		Validate.notNull(config, "Config can not be null.");
		Validate.notNull(save, "Save runnable can not be null.");
		this.config = config;
		this.save = save;
	}
	
	/*
	 * @return the configuration<br>
	 * @see ConfigManager#ConfigManager()
	 */
	public FileConfiguration getConfig() {
		return this.config;
	}

	/*
	 * Save the config<br>
	 * @see ConfigManager#getConfig()
	 */
	public void save() {
		this.save.run();
	}
}
