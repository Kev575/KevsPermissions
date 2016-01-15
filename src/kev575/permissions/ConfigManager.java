package kev575.permissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

	private KevsPermissions plugin;
	private FileConfiguration cfg;
	private FileConfiguration groups;
	private FileConfiguration players;
	public ConfigManager(KevsPermissions kevsPermissions) {
		plugin = kevsPermissions;
		plugin.saveDefaultConfig();
		cfg = plugin.getConfig();
		File config = new File(plugin.getDataFolder(), "groups.yml");
		if (!config.exists()) {
			try {
				config.createNewFile();
				getGroups().set("default.prefix", "your new prefix");
				List<String> permissions = new ArrayList<>();
				permissions.add("your.new.permission");
				getGroups().set("default.permissions", permissions);
				saveGroups();
			} catch (IOException e) {
				System.out.println("Can't create File groups.yml");
				e.printStackTrace();
				Bukkit.getPluginManager().disablePlugin(plugin);
				return;
			}
		}
		groups = YamlConfiguration.loadConfiguration(config);
		config = new File(plugin.getDataFolder(), "players.yml");
		if (!config.exists()) {
			try {
				config.createNewFile();
			} catch (IOException e) {
				System.out.println("Can't create File players.yml");
				e.printStackTrace();
				Bukkit.getPluginManager().disablePlugin(plugin);
				return;
			}
		}
		players = YamlConfiguration.loadConfiguration(config);
	}
	
	public void saveGroups() {
		File config = new File(plugin.getDataFolder(), "groups.yml");
		try {
			groups.save(config);
		} catch (IOException e) {
			System.out.println("Can't save File groups.yml");
			e.printStackTrace();
		}
	}
	
	public void savePlayers() {
		File config = new File(plugin.getDataFolder(), "players.yml");
		try {
			players.save(config);
		} catch (IOException e) {
			System.out.println("Can't save File players.yml");
			e.printStackTrace();
		}
	}
	
	public void saveConfig() {
		plugin.saveConfig();
	}
	
	public List<PlayerGroup> getPlayersGroup(UUID id) {
		List<PlayerGroup> groups = new ArrayList<>();
		if (players.isList(id + ".global.group")) {
			if (players.isString(id + "." + Bukkit.getPlayer(id).getWorld().getName() + ".group")) {
				groups.add(new PlayerGroup(players.getString(id + "." + Bukkit.getPlayer(id).getWorld().getName() + ".group")));
				return groups;
			}
			for (String str : cfg.getStringList(id.toString() + ".global.group")) {
				groups.add(new PlayerGroup(str));
			}
			return groups;
		} else {
			groups.add(new PlayerGroup("default"));
			return groups;
		}
	}
	
	public PlayerGroup getGroup(String group) {
		if (!KevsPermissions.config.getGroups().contains(group)) {
			return new PlayerGroup("default");
		}
		return new PlayerGroup(group);
	}
	
	public void setPlayersGroup(UUID id, String group) {
		players.set(id + ".global.group", group);
		savePlayers();
	}
	
	public FileConfiguration getCfg() {
		return cfg;
	}
	public FileConfiguration getGroups() {
		return groups;
	}
	public FileConfiguration getPlayers() {
		return players;
	}
}
