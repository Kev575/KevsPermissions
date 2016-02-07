package kev575.permissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

	private KevsPermissions plugin;
	private FileConfiguration cfg;
	private FileConfiguration groups;
	private FileConfiguration players;
	public ConfigManager(KevsPermissions kevsPermissions) {
		plugin = kevsPermissions;
		cfg = plugin.getConfig();
		File config = new File(plugin.getDataFolder(), "groups.yml");
		if (!config.exists()) {
			try {
				config.getParentFile().mkdirs();
				config.createNewFile();
				groups = YamlConfiguration.loadConfiguration(config);
				getGroups().set(getDefaultGroup() + ".prefix", "your new prefix");
				List<String> permissions = new ArrayList<>();
				permissions.add("your.new.permission");
				getGroups().set(getDefaultGroup() + ".permissions", permissions);
				saveGroups();
			} catch (IOException e) {
				System.out.println("Can't create file groups.yml! There could be more messages after this:");
				System.out.println("  > " + e.getMessage());
				return;
			}
		} else
			groups = YamlConfiguration.loadConfiguration(config);
		config = new File(plugin.getDataFolder(), "players.yml");
		if (!config.exists()) {
			try {
				config.getParentFile().mkdirs();
				config.createNewFile();
				players = YamlConfiguration.loadConfiguration(config);
			} catch (IOException e) {
				System.out.println("Can't create file players.yml! There could be more messages after this:");
				System.out.println("  > " + e.getMessage());
				return;
			}
		} else 
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
		
		updateVaultPermissions();
		updateVaultChat();
	}
	
	public void savePlayers() {
		File config = new File(plugin.getDataFolder(), "players.yml");
		try {
			players.save(config);
		} catch (IOException e) {
			System.out.println("Can't save File players.yml");
			e.printStackTrace();
		}
		
		updateVaultPermissions();
		updateVaultChat();
	}
	
	public void updateVaultChat() {
		if (KevsPermissions.vaultChat == null)
			return;
		for (PlayerGroup g : getAllGroups()) {
			((Chat) KevsPermissions.vaultChat).setGroupPrefix((World)null, g.getName(), g.getPrefix());
			((Chat) KevsPermissions.vaultChat).setGroupSuffix((World)null, g.getName(), g.getSuffix());
		}
	}
	
	@Deprecated
	public void updateVaultPermissions() {
	/*	if (KevsPermissions.vaultPermission == null)
			return;
		for (OfflinePlayer of : Bukkit.getOfflinePlayers()) {
			for (PlayerGroup g : getPlayerGroups(of.getUniqueId())) {
				for (String perm : g.getPermissions(false))
					((Permission) KevsPermissions.vaultPermission).playerRemove(null, of, perm);
					((Permission) KevsPermissions.vaultPermission).playerRemoveGroup(null, of, g.getName());
			}
		}
		for (OfflinePlayer of : Bukkit.getOfflinePlayers()) {
			for (PlayerGroup g : getPlayerGroups(of.getUniqueId())) {
				((Permission) KevsPermissions.vaultPermission).playerAddGroup(null, of, g.getName());
				for (String perm : g.getPermissions(false)) {
					((Permission) KevsPermissions.vaultPermission).playerAdd(null, of, perm);
				}
			}
		}*/
	}

	public PlayerGroup getDefaultGroup() {
		return getGroup(cfg.getString("default"));
	}
	
	public ArrayList<PlayerGroup> getAllGroups() {
		ArrayList<PlayerGroup> al = new ArrayList<>();
		for (String str : getGroups().getValues(false).keySet()) {
			al.add(new PlayerGroup(str));
		}
		return al;
	}
	
	public void saveConfig() {
		plugin.saveConfig();
	}
	
	public List<PlayerGroup> getPlayerGroups(UUID id) {
		List<PlayerGroup> groups = new ArrayList<PlayerGroup>();
		if (getPlayers().isList(id + ".global.group")) {
			/*if (players.isString(id + "." + Bukkit.getPlayer(id).getWorld().getName() + ".group")) {
				groups.add(new PlayerGroup(players.getString(id + "." + Bukkit.getPlayer(id).getWorld().getName() + ".group")));
				return groups;
			}*/
			for (String str : getPlayers().getStringList(id + ".global.group")) {
				groups.add(new PlayerGroup(str));
			}
			if (groups.size() == 0) {
				groups.add(getDefaultGroup());
			}
			return groups;
		} else {
			groups.add(getDefaultGroup());
			return groups;
		}
	}
	
	public PlayerGroup getGroup(String group) {
		if (!getGroups().contains(group)) {
			return null;
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
		if (groups == null)
			groups = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "groups.yml"));
		return groups;
	}
	public FileConfiguration getPlayers() {
		if (players == null)
			players = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players.yml"));
		return players;
	}
}
