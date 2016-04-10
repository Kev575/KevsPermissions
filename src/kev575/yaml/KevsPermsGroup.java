package kev575.yaml;

import kev575.permissions.KevsPermissions;

import org.bukkit.configuration.ConfigurationSection;

public class KevsPermsGroup extends YamlReader {

	public KevsPermsGroup(ConfigurationSection sec) {
		super(sec);
	}

	@Override
	public void saveConfig() {
		KevsPermissions.manager.saveGroups();
	}
	
}
