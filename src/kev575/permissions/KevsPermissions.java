package kev575.permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import kev575.yaml.KevsPermsGroup;
import kev575.yaml.KevsPermsPlayer;
import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

public class KevsPermissions extends JavaPlugin implements Listener {

	public static PermissionsManager manager;
	public String pre;
	public static Object vaultChat;
	HashMap<UUID, PermissionAttachment> atts = new HashMap<UUID, PermissionAttachment>();
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		/*if (getConfig().getBoolean("mysql.enabled")) {
			try {
				config = new KevsPermsManager(this, new KevSQL(getConfig().getString("mysql.ip"), getConfig().getString("mysql.database"), getConfig().getString("mysql.username"), getConfig().getString("mysql.passwd"), getConfig().getInt("mysql.port")));				
			} catch (Exception e) { config = new KevsPermsManager(this); }
		}*/
		/*if (config == null || config.getSql() == null) {
			config = new KevsPermsManager(this);
		}*/
		manager = new PermissionsManager(this);
		pre = "§6KevsPermission §8» §7";
		for (String sub : KevsPermissions.COMMANDS)
			Bukkit.getPluginManager().addPermission(new Permission("kp." + sub));
		
//		pre = (config.getConfig().get("prefix") != "") ? config.getConfig().getString("prefix") : pre;
		Bukkit.getPluginManager().registerEvents(this, this);
		reloadAllPlayers();
		if (manager.getPluginConfig().isBoolean("enablemanagers") && manager.getPluginConfig().getBoolean("enablemanagers") && manager.getPluginConfig().getBoolean("usevault")) {
			setupProvider();
		}
		if (manager.getPluginConfig().getBoolean("chatmanager"))
			Bukkit.getPluginManager().registerEvents(new ChatManager(), this);
		//				if (config.getConfig().getBoolean("scoreboardmanager"))
//				Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
//					ScoreboardManager man = Bukkit.getScoreboardManager();
//					Scoreboard sb = man.getMainScoreboard();
//					@SuppressWarnings("deprecation")
//					public void run() {
//						for (String groupName : config.getGroups().getKeys(false)) {
//							KevsPermsGroup gr = config.getGroup(groupName);
//							gr.fix();
//							try {
//								Team current;
//								if (sb.getTeam(groupName) == null)
//									current = sb.registerNewTeam(groupName);
//								else
//									current = sb.getTeam(groupName);
//								current.setPrefix(gr.getPrefix().replace("&", "§"));
//								for (Player p : Bukkit.getOnlinePlayers()) {
//									KevsPermsPlayer player = config.getPlayer(p.getUniqueId());
//									if (player.getMainGroup().equals(groupName)) {
//										if (!current.getPlayers().contains(p))current.addPlayer(p);
//									} else {
//										if (current.getPlayers().contains(p))current.removePlayer(p);
//									}
//								}
//							} catch (Exception e) { continue; }
//						}
//					}
//				}, 20, 20);
		if (manager.getGroup(manager.getPluginConfig().getString("default")) == null) {
			manager.getGroupsConfig().createSection(manager.getPluginConfig().getString("default"));
			new KevsPermsGroup(manager.getGroup(manager.getPluginConfig().getString("default"))).create();
			manager.saveGroups();
		}
		/*if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new EZPlaceholderHook(this, "kevspermissions") {
				@Override
				public String onPlaceholderRequest(Player arg0, String arg1) {
					if (arg1.equalsIgnoreCase("group")) {
						return config.getPlayer(arg0.getUniqueId()).getGroups().get(0);
					} else if (arg1.equalsIgnoreCase("prefix")) {
						KevsPermsPlayer player = KevsPermissions.config.getPlayer(arg0.getUniqueId());
						player.fix(arg0.getUniqueId());
						if (player.getPrefix().equalsIgnoreCase("*")) {
							player.setPrefix(KevsPermissions.config.getGroup(player.getGroups().get(0)).getPrefix());
						}
						return player.getPrefix();
					} else if (arg1.equalsIgnoreCase("suffix")) {
						KevsPermsPlayer player = KevsPermissions.config.getPlayer(arg0.getUniqueId());
						player.fix(arg0.getUniqueId());
						if (player.getSuffix().equalsIgnoreCase("*")) {
							player.setSuffix(KevsPermissions.config.getGroup(player.getGroups().get(0)).getSuffix());
						}
						return player.getPrefix();
					} else if (arg1.startsWith("hasperm-")) {
						return arg0.hasPermission(arg1.split("-")[1]) ? "§atrue" : "§cfalse";
					} else {
						return "no-data";
					}
				}
			}.hook();
		}*/
	}
	
	private void setupProvider() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return;
		}
		try {
			RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
	        if (chatProvider != null) {
	            vaultChat = chatProvider.getProvider();
	        }
		} catch (Exception e) {}
	}
	
	@Override
	public void onDisable() {
		for (PermissionAttachment at : atts.values()) {
			disablePermission(at);
		}
		atts = null; manager = null;
	}
	
	private void disablePermission(PermissionAttachment at) {
		for (String perm : at.getPermissions().keySet()) {
			at.unsetPermission(perm);
		}
	}
	
	public static final String[] COMMANDS = { "help", "setplayerperm", "setgroup", "insertgroup", "setsuffix", "creategroup", "removegroup", "setprefix", "setperm", "copyto", "listgroup", "groupinfo", "reload"};
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		ArrayList<String> l = new ArrayList<String>();
		if (cmd.getName().equalsIgnoreCase("kevspermissions")) {
			List<String> completions = new ArrayList<String>();
			if (args.length == 1) {
				String partialCommand = args[0];
				List<String> commands = new ArrayList<String>(Arrays.asList(COMMANDS));
				StringUtil.copyPartialMatches(partialCommand, commands, completions);
				Collections.sort(completions);
				return completions;
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("setgroup") || args[0].equalsIgnoreCase("setplayerperm") || args[0].equalsIgnoreCase("listgroup")) {
					String partialCommand = args[1];
					ArrayList<String> list = new ArrayList<String>();
					for (Player p : Bukkit.getOnlinePlayers()) {
						list.add(p.getName());
					}
					Set<String> commands = new HashSet<String>(list);
					StringUtil.copyPartialMatches(partialCommand, commands, completions);
					Collections.sort(completions);
					return completions;
				} else if (args[0].equalsIgnoreCase("setprefix") || args[0].equalsIgnoreCase("setsuffix") || args[0].equalsIgnoreCase("setperm") || args[0].equalsIgnoreCase("removegroup") || args[0].equalsIgnoreCase("copyto") || args[0].equalsIgnoreCase("groupinfo")) {
					String partialCommand = args[1];
					Set<String> commands = manager.getGroupsConfig().getValues(false).keySet();
					StringUtil.copyPartialMatches(partialCommand, commands, completions);
					Collections.sort(completions);
					return completions;
				}
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("setgroup") || args[0].equalsIgnoreCase("copyto")) {
					String partialCommand = args[1];
					Set<String> commands = manager.getGroupsConfig().getValues(false).keySet();
					StringUtil.copyPartialMatches(partialCommand, commands, completions);
					Collections.sort(completions);
					return completions;
				} else if (args[0].equalsIgnoreCase("setprefix")) {
					l.add("<Prefix...>");
				} else if (args[0].equalsIgnoreCase("setsuffix")) {
					l.add("<Suffix...>");
				} else if (args[0].equalsIgnoreCase("setperm") || args[0].equalsIgnoreCase("setplayerperm")) {
					l.add("<Permission>");
				}
			}
			
		}
		return l;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender se, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("kevspermissions")) {
			if (args.length == 0) {
				se.sendMessage(pre + "§6KevsPermissions§7 created by §aKev575§7 v" + getDescription().getVersion());
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("help")) {
					if (!se.hasPermission("kp.help")) {
						se.sendMessage(noPerm());
						return true;
					}
					String string = "/kp setgroup <Player> <Group> - set the group for a player\n"
							+ "/kp creategroup <Group> - create a group\n/kp removegroup <Group> - removes a group\n"
							+ "/kp setprefix <Group> <Prefix...> - set the prefix of a group\n"
							+ "/kp setperm <Group> <World>:<Permission> - adds/removes the perm of a group for a world (use * for global)\n"
							+ "/kp listgroup <Player> - show player groups / permissions\n"
							+ "/kp copyto <GroupFrom> <GroupTo> - copy and overrides the permissions of a group\n"
							+ "/kp groupinfo <Group> - show groups permissions / inherits\n"
							+ "/kp setplayerperm <Player> <World>:<Permission> - adds/removes the perm of a player for a world (use * for global)\n"
							+ "/kp insertgroup <Player> <Group> - set the 1. group of a player (overriden prefix)\n"
							+ "Permissions?! Just use kp.<subcommand> for example: kp.setgroup and kp.insertgroup";
					se.sendMessage(string.split("\n"));
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (!se.hasPermission("kp.reload")) {
						se.sendMessage(noPerm());
						return true;
					}
					reloadConfig();
					se.sendMessage("§6KevsPermissions§8> §aReloaded config. §7§oIf you want to reload the groups_json.yml or the players_json.yml restart the server (or reload it)!");
				} else {
					se.sendMessage(pre + "Use /" + label + " help");
				}
				return true;
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("creategroup")) {
					if (!se.hasPermission("kp.creategroup")) {
						se.sendMessage(noPerm());
						return true;
					}
					if (manager.getGroup(args[1]) != null) {
						se.sendMessage(pre + "The group \"" + args[1] + "\" already exists.");
						return true;
					}
					boolean created = manager.createGroup(args[1]);
					if (!created) {
						se.sendMessage(pre + "§cString is not alphanumeric (0-9,a-z,A-Z): §e" + args[1]);
					} else {
						KevsPermsGroup group = new KevsPermsGroup(manager.getGroup(args[1]));
						group.create();
						
						se.sendMessage(pre + "§aCreated group §e" + args[1] + "§a!");
						
						reloadAllPlayers();
					}
				} else if (args[0].equalsIgnoreCase("removegroup")) {
					if (!se.hasPermission("kp.removegroup")) {
						se.sendMessage(noPerm());
						return true;
					}
					if (manager.getGroup(args[1]) == null) {
						se.sendMessage(pre + "The group \"" + args[1] + "\" does not exist. :(");
						return true;
					}
					boolean created = manager.createGroup(args[1]);
					if (!created) {
						se.sendMessage(pre + "§cString is not alphanumeric (0-9,a-z,A-Z): §e" + args[1]);
					} else {
						se.sendMessage(pre + "§aRemoved group §e" + args[1] + "§a!");
						manager.getGroupsConfig().set(args[1], null);
						manager.saveGroups();
						reloadAllPlayers();
					}
				} else if (args[0].equalsIgnoreCase("groupinfo")) {
					if (!se.hasPermission("kp.groupinfo")) {
						se.sendMessage(noPerm());
						return true;
					}
					if (manager.getGroup(args[1]) == null) {
						se.sendMessage(pre + "The group \"" + args[1] + "\" does not exist. :(");
						return true;
					}
					KevsPermsGroup group = new KevsPermsGroup(manager.getGroup(args[1]));
					se.sendMessage("§eGroup§8: §7" + args[1]);
					se.sendMessage("  §ePrefix§8: §7" + group.getPrefix() + " §8(§r" + group.getPrefix().replace("&", "§") + "§8)");
					se.sendMessage("  §eSuffix§8: §7" + group.getSuffix() + " §8(§r" + group.getSuffix().replace("&", "§") + "§8)");
					se.sendMessage("  §ePermissions§8:");
					for (String perm : group.getPermissions()) {
						se.sendMessage("    §7- §a" + perm);
					}
					se.sendMessage("  §eInherits§8:");
					for (String perm : group.getInherits()) {
						se.sendMessage("    §7- §a" + perm);
					}
				} else if (args[0].equalsIgnoreCase("listgroup")) {
					if (!se.hasPermission("kp.listgroup")) {
						se.sendMessage(noPerm());
						return true;
					}
					if (!(Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore() || Bukkit.getOfflinePlayer(args[1]).isOnline())) {
						se.sendMessage(pre + "§cThat player hasn't played before or is not online!");
						return true;
					}
					UUID uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
					if (manager.getPlayer(uuid) == null) {
						se.sendMessage(pre + "§cThe player §e" + Bukkit.getOfflinePlayer(args[1]).getName() + "§c does not exist in config!");
						return true;
					}
					se.sendMessage(pre + "§aGroup(s) of §e" + args[1] + "§a:");
					se.sendMessage("§6" + new KevsPermsPlayer(manager.getPlayer(uuid)).getPermissions());
					for (String group : new KevsPermsPlayer(manager.getPlayer(uuid)).getGroups()) {
						KevsPermsGroup group2 = new KevsPermsGroup(manager.getGroup(group));
						if (group != null && group2 != null && !group2.isNull()) {
							se.sendMessage("  §eGroup§8: §7" + group);
							se.sendMessage("    §ePrefix§8: §7" + group2.getPrefix() + " §8(§r" + group2.getPrefix().replace("&", "§") + "§8)");
							se.sendMessage("    §eSuffix§8: §7" + group2.getSuffix() + " §8(§r" + group2.getSuffix().replace("&", "§") + "§8)");
						}
					}
				} else {
					se.sendMessage(pre + "Use /" + label + " help");
				}
			} else if (args.length>=3 && args[0].equalsIgnoreCase("setprefix")) {
				if (!se.hasPermission("kp.setprefix")) {
					se.sendMessage(noPerm());
					return true;
				}
				if (manager.getGroup(args[1]) == null) {
					se.sendMessage(pre + "The group \"" + args[1] + "\" doesn't exist. :(");
					return true;
				}
				String s = "";
				for (int i = 2; i < args.length; i++) {
					s += args[i] + " ";
				}
				s = s.substring(0, s.length()-1);
				KevsPermsGroup group = new KevsPermsGroup(manager.getGroup(args[1]));
				group.setPrefix(s);
				se.sendMessage(pre + "§aPrefix of §e" + args[1] + " §achanged to §r" + s.replace("&", "§"));
				return true;
			} else if (args.length>=3 && args[0].equalsIgnoreCase("setsuffix")) {
				if (!se.hasPermission("kp.setsuffix")) {
					se.sendMessage(noPerm());
					return true;
				}
				if (manager.getGroup(args[1]) == null) {
					se.sendMessage(pre + "The group \"" + args[1] + "\" doesn't exist. :(");
					return true;
				}
				String s = "";
				for (int i = 2; i < args.length; i++) {
					s += args[i] + " ";
				}
				s = s.substring(0, s.length()-1);
				KevsPermsGroup group = new KevsPermsGroup(manager.getGroup(args[1]));
				group.setSuffix(s);
				se.sendMessage(pre + "§aSuffix of §e" + args[1] + " §achanged to §r" + s);
				return true;
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("setgroup")) {
					if (!se.hasPermission("kp.setgroup")) {
						se.sendMessage(noPerm());
						return true;
					}
					if (manager.getGroup(args[2]) == null) {
						se.sendMessage(pre + "The group \"" + args[2] + "\" doesn't exist. :(");
						return true;
					}
					UUID uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
					KevsPermsPlayer player = new KevsPermsPlayer(manager.getPlayer(uuid));
					List<String> groups = player.getGroups();
					if (groups.contains(args[2])) {
						groups.remove(args[2]);
						se.sendMessage(pre + "§aRemoved group §e" + args[2] + "§a from§e " + args[1] + "§a!");
					} else {
						groups.add(args[2]);
						se.sendMessage(pre + "§aAdded group §e" + args[2] + "§a from§e " + args[1] + "§a!");
					}
					player.setGroups(groups);
					reloadAllPlayers();
				} else if (args[0].equalsIgnoreCase("insertgroup")) {
					if (!se.hasPermission("kp.insertgroup")) {
						se.sendMessage(noPerm());
						return true;
					}
					if (manager.getGroup(args[2]) == null) {
						se.sendMessage(pre + "The group \"" + args[2] + "\" doesn't exist. :(");
						return true;
					}
					UUID uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
					KevsPermsPlayer player = new KevsPermsPlayer(manager.getPlayer(uuid));
					List<String> groups = player.getGroups();
					if (groups.contains(args[2])) {
						se.sendMessage(pre + "§cPlease use §9/" + label + " setgroup " + args[2]);
						return true;
					} else {
						groups.add(0, args[2]);
						se.sendMessage(pre + "§aAdded 1. group §e" + args[2] + "§a from§e " + args[1] + "§a!");
					}
					player.setGroups(groups);
					
					reloadAllPlayers();
				} else if (args[0].equalsIgnoreCase("setperm")) {
					if (!se.hasPermission("kp.setperm")) {
						se.sendMessage(noPerm());
						return true;
					}
					if (manager.getGroup(args[1]) == null) {
						se.sendMessage(pre + "§cThe group \"" + args[1] + "\" doesn't exist. :(");
						return true;
					}
					/*if (args[2].split(":").length <= 1) {
						se.sendMessage(pre + "§cPlease use <world>:<permission>. For global use *:<permission>.");
						return true;
					}*/
					KevsPermsGroup group = new KevsPermsGroup(manager.getGroup(args[1]));
					List<String> perms = group.getPermissions();
					boolean alpha = manager.isAlphanumeric(args[2]);
					if (!alpha) {
						se.sendMessage(pre + "§cThe given string §e" + args[2] + " §cis not alphanumeric!");
						return true;
					}
					if (perms.contains(args[2])) {
						perms.remove(args[2]);
						se.sendMessage(pre + "§aRemoved permission §e" + args[2] + "§a from group §e" + args[1] + "§a!");
					} else {
						perms.add(args[2]);
						se.sendMessage(pre + "§aAdded permission §e" + args[2] + "§a to group §e" + args[1] + "§a!");
					}
					/*ArrayList<String> s = perms.get(args[2].split(":")[0]);
					perms.remove(args[2].split(":")[0]);
					if (s == null) {
						s = new ArrayList<String>();
					}
					if (s.contains(args[2].split(":")[1])) {
						se.sendMessage(pre + "§aRemoved permission §e" + args[2].split(":")[1] + "§a at world §e" + args[2].split(":")[0] + "§a from group §e" + args[1] + "§a!");
						s.remove(args[2].split(":")[1]);
					} else {
						s.add(args[2].split(":")[1]);
						se.sendMessage(pre + "§aAdded permission §e" + args[2].split(":")[1] + "§a at world §e" + args[2].split(":")[0] + "§a to group §e" + args[1] + "§a!");
					}
					perms.put(args[2].split(":")[0], s);*/
					group.setPermissions(perms);
					
					reloadAllPlayers();
				} else if (args[0].equalsIgnoreCase("setplayerperm")) {
					if (!se.hasPermission("kp.setplayerperm")) {
						se.sendMessage(noPerm());
						return true;
					}
					/*if (args[2].split(":").length <= 1) {
						se.sendMessage(pre + "§cPlease use <world>:<permission>. For global use *:<permission>.");
						return true;
					}*/
					UUID uniqueId = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
					
					KevsPermsPlayer player = new KevsPermsPlayer(manager.getPlayer(uniqueId));
					
					List<String> perms = player.getPermissions();
					if (perms.contains(args[2].split(":")[1])) {
						perms.remove(args[2].split(":")[1]);
						se.sendMessage(pre + "§aRemoved permission §e" + args[2] + "§a from player §e" + args[1] + "§a!");
					} else {
						perms.add(args[2].split(":")[1]);
						se.sendMessage(pre + "§aAdded permission §e" + args[2] + "§a to player §e" + args[1] + "§a!");
					}
					
					/*ArrayList<String> s = perms.get(args[2].split(":")[0]);
					perms.remove(args[2].split(":")[0]);
					if (s == null) {
						s = new ArrayList<>();
					}
					if (s.contains(args[2].split(":")[1])) {
						se.sendMessage(pre + "§aRemoved permission §e" + args[2].split(":")[1] + "§a at world §e" + args[2].split(":")[0] + "§a from player §e" + args[1] + "§a!");
						s.remove(args[2].split(":")[1]);
					} else {
						s.add(args[2].split(":")[1]);
						se.sendMessage(pre + "§aAdded permission §e" + args[2].split(":")[1] + "§a at world §e" + args[2].split(":")[0] + "§a to player §e" + args[1] + "§a!");
					}
					perms.put(args[2].split(":")[0], s);
					manager.savePlayer(uniqueId, player);*/
					player.setPermissions(perms);
					reloadAllPlayers();
				} else if (args[0].equalsIgnoreCase("copyto")) {
					if (!se.hasPermission("kp.copyto")) {
						se.sendMessage(noPerm());
						return true;
					}
					
					KevsPermsGroup groupFrom = new KevsPermsGroup(manager.getGroup(args[1]));
					KevsPermsGroup groupTo = new KevsPermsGroup(manager.getGroup(args[2]));
					if (groupTo.isNull()) {
						se.sendMessage(pre + "§cThe group " + args[2] + " is not valid.");
						return true;
					}
					if (groupTo.isNull()) {
						se.sendMessage(pre + "§cThe group " + args[1] + " is not valid.");
						return true;
					}
					if (args[1].equals(args[2])) {
						se.sendMessage(pre + "§cThat groups are equals...");
						return true;
					}
					groupTo.setPermissions(groupFrom.getPermissions());
					
					se.sendMessage(pre + "§aSet every permission from group §e" + args[1] + " §ato group §e" + args[2] + "§a!");
					return true;
				} else {
					se.sendMessage(pre + "Use /" + label + " help");
				}
			} else {
				se.sendMessage(pre + "Use /" + label + " help");
			}
		}
		return true;
	}
	
	private void reloadAllPlayers() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			join(new PlayerJoinEvent(player, ""));
		}
	}

	private String noPerm() {
		return pre + "§cYou don't have the permission(s) to do that.";
	}

	@EventHandler
	public void join(final PlayerJoinEvent e) {
		
		if (manager.getPluginConfig().getBoolean("joinmessage")) {
			e.getPlayer().sendMessage(pre + "§7Setting up your permissions...");
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
			@Override
			public void run() {
				if (atts.containsKey(e.getPlayer().getUniqueId())) {
					disablePermission(atts.get(e.getPlayer().getUniqueId()));
					atts.remove(e.getPlayer().getUniqueId());
				}
				PermissionAttachment at = e.getPlayer().addAttachment(KevsPermissions.getPlugin(KevsPermissions.class), 1728000);
				KevsPermsPlayer player = new KevsPermsPlayer(manager.getPlayer(e.getPlayer().getUniqueId()));
				if (player.isNull()) {
					manager.createPlayer(e.getPlayer().getUniqueId());
				}
				if (player.getGroups().size() == 0) {
					ArrayList<String> list = new ArrayList<>();
					list.add(manager.getPluginConfig().getString("default"));
					player.setGroups(list);
				}
				for (String groupName : player.getGroups()) {
					for (String permission : new KevsPermsGroup(manager.getGroup(groupName)).getPermissions()) {
						if (!at.getPermissions().containsKey(permission)) {
							if (permission.startsWith("-")) {
								at.setPermission(permission.substring(1), false);
							} else {
								at.setPermission(permission, true);
							}
						}
					}
					for (String groupInherit : new KevsPermsGroup(manager.getGroup(groupName)).getInherits()) {
						for (String permission : new KevsPermsGroup(manager.getGroup(groupInherit)).getPermissions()) {
							if (!at.getPermissions().containsKey(permission)) {
								if (permission.startsWith("-")) {
									at.setPermission(permission.substring(1), false);
								} else {
									at.setPermission(permission, true);
								}
							}
						}
					}
				}
				
				for (String permission : player.getPermissions()) {
					if (!at.getPermissions().containsKey(permission)) {
						if (permission.startsWith("-")) {
							at.setPermission(permission.substring(1), false);
						} else {
							at.setPermission(permission, true);
						}
					}
				}
				
				if (manager.getPluginConfig().getBoolean("joinmessage")) {
					e.getPlayer().sendMessage(pre + "§7Done!");
				}
			}
		});
		
		/*Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (atts.containsKey(e.getPlayer().getUniqueId())) {
					disablePermission(atts.get(e.getPlayer().getUniqueId()));
					atts.remove(e.getPlayer().getUniqueId());
				}
				PermissionAttachment at = e.getPlayer().addAttachment(KevsPermissions.getPlugin(KevsPermissions.class), 1728000);
				applyToAt(at, e.getPlayer().getUniqueId(), e.getPlayer().getWorld().getName(), null);
				KevsPermsPlayer player = manager.getPlayer(e.getPlayer().getUniqueId());
				if (player.getGroupsConfig().size() == 0) {
					ArrayList<String> list = new ArrayList<>();
					list.add(manager.getConfig().getString("default"));
					player.setGroups(list);
					manager.savePlayer(e.getPlayer().getUniqueId(), player);
				}
				ArrayList<String> global = player.getPermissions().get("*");
				if (global != null)
					for (String perm : global) {
						at.setPermission(perm.startsWith("-") ? perm.substring(1, perm.length()) : perm, perm.startsWith("-") ? false : true);
					}
				ArrayList<String> worldPerms = player.getPermissions().get(e.getPlayer().getWorld().getName());
				if (worldPerms != null) {
					for (String perm : worldPerms) {
						at.setPermission(perm.startsWith("-") ? perm.substring(1, perm.length()) : perm, perm.startsWith("-") ? false : true);
					}
				}
				atts.put(e.getPlayer().getUniqueId(), at);
				if (manager.getConfig().getBoolean("tabmanager")) {
					if (player.getGroupsConfig().size() != 0) {
						KevsPermsGroup group = manager.getGroup(manager.getPlayer(e.getPlayer().getUniqueId()).getGroups().get(0));
						if (group != null)
							e.getPlayer().setPlayerListName(group.getPrefix().replace("&", "§") + e.getPlayer().getName());
					}
				}
			}
		});
		t.start();*/
	}
	
	/*protected void applyToAt(PermissionAttachment at, UUID id, String worldName, ArrayList<String> inherits) {
		if (id != null)
		for (String groupName : manager.getPlayer(id).getGroups()) {
			try {
				KevsPermsGroup group = manager.getGroup(groupName);
				if (group != null) {
					for (String perm : group.getPermissions().get("*")) {
						at.setPermission(perm.startsWith("-") ? perm.substring(1, perm.length()) : perm, perm.startsWith("-") ? false : true);
					}
					applyToAt(at, null, null, group.getInherits());
					if (worldName != null) {
						ArrayList<String> worldPerms = group.getPermissions().get(worldName);
						if (worldPerms != null) {
							for (String perm : worldPerms) {
								at.setPermission(perm.startsWith("-") ? perm.substring(1, perm.length()) : perm, perm.startsWith("-") ? false : true);
							}
						}
					}
				}
			} catch (Exception e2) {}
		}
		else {
			for (String groupName : inherits) {
				KevsPermsGroup group = manager.getGroup(groupName);
				if (group != null)
					for (String perm : group.getPermissions().get("*")) {
						at.setPermission(perm.startsWith("-") ? perm.substring(1, perm.length()) : perm, perm.startsWith("-") ? false : true);
					}
			}
		}
	}*/

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (atts.containsKey(e.getPlayer().getUniqueId())) {
			disablePermission(atts.get(e.getPlayer().getUniqueId()));
			atts.remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
//		join(new PlayerJoinEvent(e.getPlayer(), null));
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (manager.getPluginConfig().getBoolean("antibuild")) {
			if (!e.getPlayer().hasPermission("kp.antibuild")) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(noPerm());
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (manager.getPluginConfig().getBoolean("antibuild")) {
			if (!e.getPlayer().hasPermission("kp.antibuild")) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(noPerm());
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (manager.getPluginConfig().getBoolean("antibuild")) {
			if (!e.getPlayer().hasPermission("kp.antibuild")) {
				e.setCancelled(true);
				e.setUseItemInHand(Result.DENY);
				e.setUseInteractedBlock(Result.DENY);
				e.getPlayer().sendMessage(noPerm());
			}
		}
	}
	
	@EventHandler
	public void onPlayerDmg(EntityDamageByEntityEvent e) {
		if (manager.getPluginConfig().getBoolean("antibuild")) {
			if (e.getDamager() instanceof Player) {
				if (!((Player)e.getDamager()).hasPermission("kp.antibuild")) {
					e.setCancelled(true);
				}
			}
			if (e.getEntity() instanceof Player) {
				if (!((Player)e.getEntity()).hasPermission("kp.anitbuild")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onShear(PlayerShearEntityEvent e) {
		if (manager.getPluginConfig().getBoolean("antibuild")) {
			if (!e.getPlayer().hasPermission("kp.antibuild"))
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(PlayerJoinEvent e) {
		if (manager.getPluginConfig().getBoolean("antibuild")) {
			if (!e.getPlayer().hasPermission("kp.antibuild"))
				e.getPlayer().sendMessage("§6KevsPermissions §8> §cYou currently lack the permission §7\"§akp.antibuild§7\"§c!");
		}
	}
}
 