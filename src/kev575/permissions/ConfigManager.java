package kev575.permissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kev575.sql.KevSQL;
import kev575.sql.SQLManager;
import net.milkbowl.vault.chat.Chat;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

	private KevsPermissions plugin;
	private FileConfiguration cfg;
	private FileConfiguration groups;
	private FileConfiguration players;
	private SQLManager manager;
	
	public void setGroups(FileConfiguration groups) {
		this.groups = groups;
	}

	public void setPlayers(FileConfiguration players) {
		this.players = players;
	}

	public ConfigManager(KevsPermissions kevsPermissions) {
		plugin = kevsPermissions;
		cfg = plugin.getConfig();
		if (cfg.getBoolean("mysql.enable")) {
			try {
				manager = new SQLManager(cfg.getString("mysql.host"), cfg.getString("mysql.database"), cfg.getString("mysql.user"), cfg.getString("mysql.password"), cfg.getInt("mysql.port"));
				manager.connect();
			} catch (Exception e) { System.out.println("Can not connect to SQL server. There may be more messages after this.\n  > " + e.getMessage()); if (e.getSuppressed()[0] != null) System.out.println("    > " + e.getSuppressed()[0].getMessage()); manager = null; }
		}
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
	
	public SQLManager getSQLManager() {
		return manager;
	}
	
	public void saveGroups() {
		File config = new File(plugin.getDataFolder(), "groups.yml");
		try {
			groups.save(config);
		} catch (IOException e) {
			System.out.println("Can't save File groups.yml");
			e.printStackTrace();
		}
		KevSQL.save();
		
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
		KevSQL.save();
		
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
		KevSQL.get();
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
		KevSQL.get();
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
		KevSQL.get();
		if (!getGroups().contains(group)) {
			return null;
		}
		return new PlayerGroup(group);
	}
	
	public void setPlayersGroup(UUID id, String group) {
		players.set(id + ".global.group", group);
		savePlayers();
		KevSQL.save();
	}
	
	public FileConfiguration getCfg() {
		KevSQL.get();
		return cfg;
	}
	public FileConfiguration getGroups() {
		KevSQL.get();
		if (groups == null)
			groups = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "groups.yml"));
		return groups;
	}
	public FileConfiguration getPlayers() {
		KevSQL.get();
		if (players == null)
			players = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players.yml"));
		return players;
	}
}
