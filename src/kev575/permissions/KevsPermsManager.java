package kev575.permissions;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import kev575.json.KevsPermsGroup;
import kev575.json.KevsPermsPlayer;
import kev575.sql.KevSQL;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class KevsPermsManager {
	
	private KevsPermissions plugin;
	private FileConfiguration cfg;
	private FileConfiguration groups;
	private FileConfiguration players;
	
	private KevSQL sql;

	public KevsPermsManager(KevsPermissions kevsPermissions) {
		plugin = kevsPermissions;
		cfg = plugin.getConfig();
		File groupsFile = new File(plugin.getDataFolder(), "groups_json.yml");
		File playersFile = new File(plugin.getDataFolder(), "players_json.yml");
		if (!groupsFile.exists()) {
			try {
				groupsFile.getParentFile().mkdirs();
				groupsFile.createNewFile();
			} catch (IOException e) {
				System.out.println("Can not create groups_json.yml!!!: " + e.getMessage());
				Bukkit.getPluginManager().disablePlugin(plugin);
				return;
			}			
		}
		if (!playersFile.exists()) {
			try {
				playersFile.getParentFile().mkdirs();
				playersFile.createNewFile();
			} catch (IOException e) {
				System.out.println("Can not create players_json.yml!!!: " + e.getMessage());
				Bukkit.getPluginManager().disablePlugin(plugin);
				return;
			}
		}
		groups = YamlConfiguration.loadConfiguration(groupsFile);
		players = YamlConfiguration.loadConfiguration(playersFile);
	}

	public KevSQL getSql() {
		return sql;
	}
	
	public KevsPermsManager(KevsPermissions kevsPermissions, KevSQL kevSQL) {
		sql = kevSQL;
		cfg = plugin.getConfig();
		sql.connect();
		sql.defaultTables();
	}

	public FileConfiguration getConfig() {
		return cfg;
	}

	public FileConfiguration getGroups() {
		return groups;
	}
	
	public FileConfiguration getPlayers() {
		return players;
	}
	
	public KevsPermsPlayer getPlayer(UUID uniqueId) {
		if (getSql() != null) {
			ResultSet set = getSql().query("SELECT * FROM kevspermissions_players WHERE uuid='" + uniqueId.toString() + "'");
			KevsPermsPlayer player = null;
			try {
				player = new Gson().fromJson(set.getString("gson"), KevsPermsPlayer.class);
				player.fix();
				set.close();
			} catch (JsonSyntaxException | SQLException e) { if (player == null) {player = new KevsPermsPlayer(); player.fix();} }
			return player;
		}
		String json = (String) getPlayers().get(uniqueId.toString());
		if (json == null) {
			return null;
		}
		try {
			KevsPermsPlayer player = new Gson().fromJson(json, KevsPermsPlayer.class);
			player.fix();
			return player;
		} catch (JsonSyntaxException e) {
			return null;
		}
	}
	
	public void savePlayer(UUID uniqueId, KevsPermsPlayer player) {
		if (getSql() != null) {
			getSql().update("DELETE FROM kevspermissions_players WHERE uuid='" + uniqueId.toString() + "'");
			getSql().update("INSERT INTO `kevspermissions_players` (`uuid`, `gson`) VALUES ('" + uniqueId + "', '" + new Gson().toJson(player) + "');");
			return;
		}
		getPlayers().set(uniqueId.toString(), new Gson().toJson(player));
		savePlayers();
	}
	
	public KevsPermsGroup getGroup(String name) {
		if (getSql() != null) {
			ResultSet set = getSql().query("SELECT * FROM kevspermissions_groups WHERE groupname='" + name + "'");
			KevsPermsGroup player = null;
			try {
				player = new Gson().fromJson(set.getString("gson"), KevsPermsGroup.class);
				player.fix();
				set.close();
			} catch (JsonSyntaxException | SQLException e) { if (player == null) {player = new KevsPermsGroup(); player.fix();} }
			return player;
		}
		String json = (String) getGroups().get(name);
		if (json == null) {
			return null;
		}
		try {
			KevsPermsGroup group = new Gson().fromJson(json, KevsPermsGroup.class);
			group.fix();
			return group;
		} catch (JsonSyntaxException e) {
			return null;
		}
	}
	
	public void saveGroup(String name, KevsPermsGroup group) {
		if (getSql() != null) {
			getSql().update("DELETE FROM kevspermissions_groups WHERE groupname='" + name + "'");
			getSql().update("INSERT INTO `kevspermissions_groups` (`groupname`, `gson`) VALUES ('" + name + "', '" + new Gson().toJson(group) + "');");
			return;
		}
		if (group == null)
			getGroups().set(name, null);
		else
			getGroups().set(name, new Gson().toJson(group));
		saveGroups();
	}
	
	public ArrayList<KevsPermsGroup> getAllGroups() {
		ArrayList<KevsPermsGroup> groups = new ArrayList<>();
		for (String string : getGroups().getKeys(false)) {
			KevsPermsGroup currentGroup = getGroup(string);
			if (currentGroup != null) {
				groups.add(currentGroup);
			}
		}
		return groups;
	}
	
	public void saveGroups() {
		File config = new File(plugin.getDataFolder(), "groups_json.yml");
		try {
			groups.save(config);
		} catch (IOException e) {
			System.out.println("Can't save File groups_json.yml");
			e.printStackTrace();
		}
	}

	public void savePlayers() {
		File config = new File(plugin.getDataFolder(), "players_json.yml");
		try {
			players.save(config);
		} catch (IOException e) {
			System.out.println("Can't save File players_json.yml");
			e.printStackTrace();
		}
	}
	
}
	
//	private KevsPermissions plugin;
//	private FileConfiguration cfg;
//	private FileConfiguration groups;
//	private FileConfiguration players;
//	
//	public void setGroups(FileConfiguration groups) {
//		this.groups = groups;
//	}
//
//	public void setPlayers(FileConfiguration players) {
//		this.players = players;
//	}
//
//	public ConfigManager(KevsPermissions kevsPermissions) {
//		plugin = kevsPermissions;
//		cfg = plugin.getConfig();
//		if (cfg.getBoolean("mysql.enable")) {
//			try {
//				manager = new SQLManager(cfg.getString("mysql.host"), cfg.getString("mysql.database"), cfg.getString("mysql.user"), cfg.getString("mysql.password"), cfg.getInt("mysql.port"));
//				manager.connect();
//			} catch (Exception e) { System.out.println("Can not connect to SQL server. There may be more messages after this.\n  > " + e.getMessage()); if (e.getSuppressed()[0] != null) System.out.println("    > " + e.getSuppressed()[0].getMessage()); manager = null; }
//		}
//		File config = new File(plugin.getDataFolder(), "groups.yml");
//		if (!config.exists()) {
//			try {
//				config.getParentFile().mkdirs();
//				config.createNewFile();
//				groups = YamlConfiguration.loadConfiguration(config);
//				getGroups().set(getDefaultGroup() + ".prefix", "your new prefix");
//				List<String> permissions = new ArrayList<>();
//				permissions.add("your.new.permission");
//				getGroups().set(getDefaultGroup() + ".permissions", permissions);
//				saveGroups();
//			} catch (IOException e) {
//				System.out.println("Can't create file groups.yml! There could be more messages after this:");
//				System.out.println("  > " + e.getMessage());
//				return;
//			}
//		} else
//			groups = YamlConfiguration.loadConfiguration(config);
//		config = new File(plugin.getDataFolder(), "players.yml");
//		if (!config.exists()) {
//			try {
//				config.getParentFile().mkdirs();
//				config.createNewFile();
//				players = YamlConfiguration.loadConfiguration(config);
//			} catch (IOException e) {
//				System.out.println("Can't create file players.yml! There could be more messages after this:");
//				System.out.println("  > " + e.getMessage());
//				return;
//			}
//		} else 
//			players = YamlConfiguration.loadConfiguration(config);
//	}
//	
//	public void saveGroups() {
//		File config = new File(plugin.getDataFolder(), "groups.yml");
//		try {
//			groups.save(config);
//		} catch (IOException e) {
//			System.out.println("Can't save File groups.yml");
//			e.printStackTrace();
//		}
//		
//		updateVaultPermissions();
//		updateVaultChat();
//	}
//	
//	public void savePlayers() {
//		File config = new File(plugin.getDataFolder(), "players.yml");
//		try {
//			players.save(config);
//		} catch (IOException e) {
//			System.out.println("Can't save File players.yml");
//			e.printStackTrace();
//		}
//		
//		updateVaultPermissions();
//		updateVaultChat();
//	}
//	
//	public void updateVaultChat() {
//		if (KevsPermissions.vaultChat == null)
//			return;
//		for (PlayerGroup g : getAllGroups()) {
//			((Chat) KevsPermissions.vaultChat).setGroupPrefix((World)null, g.getName(), g.getPrefix());
//			((Chat) KevsPermissions.vaultChat).setGroupSuffix((World)null, g.getName(), g.getSuffix());
//		}
//	}
//	
//	@Deprecated
//	public void updateVaultPermissions() {
//	/*	if (KevsPermissions.vaultPermission == null)
//			return;
//		for (OfflinePlayer of : Bukkit.getOfflinePlayers()) {
//			for (PlayerGroup g : getPlayerGroups(of.getUniqueId())) {
//				for (String perm : g.getPermissions(false))
//					((Permission) KevsPermissions.vaultPermission).playerRemove(null, of, perm);
//					((Permission) KevsPermissions.vaultPermission).playerRemoveGroup(null, of, g.getName());
//			}
//		}
//		for (OfflinePlayer of : Bukkit.getOfflinePlayers()) {
//			for (PlayerGroup g : getPlayerGroups(of.getUniqueId())) {
//				((Permission) KevsPermissions.vaultPermission).playerAddGroup(null, of, g.getName());
//				for (String perm : g.getPermissions(false)) {
//					((Permission) KevsPermissions.vaultPermission).playerAdd(null, of, perm);
//				}
//			}
//		}
//	}
//
//	public PlayerGroup getDefaultGroup() {
//		return getGroup(cfg.getString("default"));
//	}
//	
//	public ArrayList<PlayerGroup> getAllGroups() {
//		ArrayList<PlayerGroup> al = new ArrayList<>();
//		for (String str : getGroups().getValues(false).keySet()) {
//			al.add(new PlayerGroup(str));
//		}
//		return al;
//	}
//	
//	public void saveConfig() {
//		plugin.saveConfig();
//	}
//	
//	public List<PlayerGroup> getPlayerGroups(UUID id) {
//		List<PlayerGroup> groups = new ArrayList<PlayerGroup>();
//		if (getPlayers().isList(id + ".global.group")) {
//		//if (players.isString(id + "." + Bukkit.getPlayer(id).getWorld().getName() + ".group")) {
//			//	groups.add(new PlayerGroup(players.getString(id + "." + Bukkit.getPlayer(id).getWorld().getName() + ".group")));
//			//	return groups;
//			//}
//			for (String str : getPlayers().getStringList(id + ".global.group")) {
//				groups.add(new PlayerGroup(str));
//			}
//			if (groups.size() == 0) {
//				groups.add(getDefaultGroup());
//			}
//			return groups;
//		} else {
//			groups.add(getDefaultGroup());
//			return groups;
//		}
//	}
//	
//	public PlayerGroup getGroup(String group) {
//		if (!getGroups().contains(group)) {
//			return null;
//		}
//		return new PlayerGroup(group);
//	}
//	
//	public void setPlayersGroup(UUID id, String group) {
//		players.set(id + ".global.group", group);
//		savePlayers();
//	}
//	
//	public FileConfiguration getCfg() {
//		return cfg;
//	}
//	public FileConfiguration getGroups() {
//		if (groups == null)
//			groups = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "groups.yml"));
//		return groups;
//	}
//	public FileConfiguration getPlayers() {
//		if (players == null)
//			players = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players.yml"));
//		return players;
//	}*/
//}
