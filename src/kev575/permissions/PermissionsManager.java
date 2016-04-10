package kev575.permissions;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import kev575.yaml.KevsPermsPlayer;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PermissionsManager {

	private KevsPermissions plugin;
	private FileConfiguration players, groups, config;
	
	protected PermissionsManager(KevsPermissions p) {
		plugin = p;
		getPlugin().saveConfig();
		config = getPlugin().getConfig();
		File groupsFile = new File(plugin.getDataFolder(), "groups.yml");
		File playersFile = new File(plugin.getDataFolder(), "players.yml");
		plugin.saveResource("groups.yml", false);
		plugin.saveResource("players.yml", false);
		groups = YamlConfiguration.loadConfiguration(groupsFile);
		players = YamlConfiguration.loadConfiguration(playersFile);
	}
	
	public KevsPermissions getPlugin() {
		return plugin;
	}
	
	public ConfigurationSection getPlayer(UUID uuid) {
		return players.getConfigurationSection(uuid.toString());
	}
	@Deprecated
	public ConfigurationSection getPlayer(String uuid) {
		return players.getConfigurationSection(uuid);
	}
	
	public ConfigurationSection getGroup(String name) {
		return groups.getConfigurationSection(name);
	}
	public FileConfiguration getPluginConfig() {
		return config;
	}
	public FileConfiguration getPlayersConfig() {
		return players;
	}
	public FileConfiguration getGroupsConfig() {
		return groups;
	}

	public void saveGroups() {
		try {
			groups.save(new File(plugin.getDataFolder(), "groups.yml"));
		} catch (IOException e) {}
	}
	
	public void savePlayers() {
		try {
			groups.save(new File(plugin.getDataFolder(), "players.yml"));
		} catch (IOException e) {}
	}

	public boolean createGroup(String string) {
		if (!isAlphanumeric(string)) {
			return false;
		} else {
			groups.createSection(string);
			saveGroups();
			return true;
		}
	}
	
	public boolean removeGroup(String string) {
		if (!isAlphanumeric(string)) {
			return false;
		} else {
			groups.set(string, null);
			saveGroups();
			return true;
		}
	}
	public boolean isAlphanumeric(String name) {
		for (char c : name.toCharArray()) {
			if (!Character.isAlphabetic(c) && c != '_' && c != '-' && !Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	public void createPlayer(UUID uniqueId) {
		players.createSection(uniqueId.toString());
		KevsPermsPlayer player = new KevsPermsPlayer(getPlayer(uniqueId));
		player.create();
		saveGroups();
	}
}
