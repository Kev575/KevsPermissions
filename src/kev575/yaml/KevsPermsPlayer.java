package kev575.yaml;

import java.util.Arrays;
import java.util.List;

import kev575.permissions.KevsPermissions;

import org.bukkit.configuration.ConfigurationSection;

public class KevsPermsPlayer extends YamlReader {

	public KevsPermsPlayer(ConfigurationSection sec) {
		super(sec);
	}

	@Override
	public void saveConfig() {
		KevsPermissions.manager.savePlayers();
	}

	public void setGroups(List<String> groups) {
		getSection().set("groups", groups);
		saveConfig();
	}
	public List<String> getGroups() {
		return section.getStringList("groups");
	}
	
	@Override
	public void create() {
		super.create();
		setGroups(Arrays.asList("default"));
	}
	
}
