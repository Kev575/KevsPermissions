package kev575.permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import kev575.obf.PluginSetInPerms;
import kev575.obf.PluginSetOutConfig;
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
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;

public class KevsPermissions extends JavaPlugin implements Listener {

	public static ConfigManager config;
	public String pre;
	public static Object vaultChat;
	HashMap<UUID, PermissionAttachment> atts = new HashMap<UUID, PermissionAttachment>();
	
	@Override
	public void onEnable() {
		config = new ConfigManager(this);
		pre = "§6KevsPermission §8> §7";
		saveDefaultConfig();
		PluginSetInPerms.a();
		PluginSetOutConfig.a(getConfig());
		PluginSetOutConfig.b(this);
		pre = config.getCfg().isString("prefix") ? config.getCfg().getString("prefix") : pre;
		Bukkit.getPluginManager().registerEvents(this, this);
		for (Player p : Bukkit.getOnlinePlayers()) {
			onJoin(new PlayerJoinEvent(p, ""));
		}
		if (config.getCfg().isBoolean("enablemanagers") && config.getCfg().getBoolean("enablemanagers")) {
			setupProvider();
		}
		if (config.getCfg().isBoolean("enablemanagers") && config.getCfg().isBoolean("scoreboardmanager")) {
			if (config.getCfg().getBoolean("enablemanagers")) {
				Bukkit.getPluginManager().registerEvents(new ChatManager(), this);
				if (config.getCfg().getBoolean("scoreboardmanager"))
				Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
					ScoreboardManager man = Bukkit.getScoreboardManager();
					Scoreboard sb = man.getMainScoreboard();
					@SuppressWarnings("deprecation")
					public void run() {
						for (PlayerGroup gr : config.getAllGroups()) {
							Team current;
							if (sb.getTeam(gr.getName()) == null)
								current = sb.registerNewTeam(gr.getName());
							else
								current = sb.getTeam(gr.getName());
							current.setPrefix(gr.getPrefix().replace("&", "§"));
							for (Player p : Bukkit.getOnlinePlayers()) {
								if (config.getPlayerGroups(p.getUniqueId()).get(0).getName().equals(gr.getName())) {
									current.addPlayer(p);
								}
							}
						}
					}
				}, 20, 20);
			}
		}
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
	      /*  RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
	        if (permissionProvider != null)
	        	vaultPermission = permissionProvider.getProvider();*/
		} catch (Exception e) {}
	}

	@Override
	public void onDisable() {
		for (PermissionAttachment at : atts.values()) {
			for (String perm : at.getPermissions().keySet()) {
				at.unsetPermission(perm);
			}
		}
		atts = null; config = null;
	}
	
	private void disablePermission(PermissionAttachment at) {
		for (String perm : at.getPermissions().keySet()) {
			at.unsetPermission(perm);
		}
	}
	
	public static final String[] COMMANDS = { "help", "setgroup", "setsuffix", "creategroup", "removegroup", "setprefix", "setperm", "addsub", "listgroup", "getgroup", "reload"};
	
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
				if (args[0].equalsIgnoreCase("setgroup")) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						l.add(p.getName());
					}
				} else if (args[0].equalsIgnoreCase("setwgroup")) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						l.add(p.getName());
					}
				} else if (args[0].equalsIgnoreCase("listgroup")) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						l.add(p.getName());
					}
				} else if (args[0].equalsIgnoreCase("removegroup") || args[0].equalsIgnoreCase("addsub") || args[0].equalsIgnoreCase("getgroup")) {
					String partialCommand = args[0];
					Set<String> commands = config.getGroups().getValues(false).keySet();
					StringUtil.copyPartialMatches(partialCommand, commands, completions);
					Collections.sort(completions);
					return completions;
				} else if (args[0].equalsIgnoreCase("setprefix") || args[0].equalsIgnoreCase("setsuffix")) {
					String partialCommand = args[0];
					Set<String> commands = config.getGroups().getValues(false).keySet();
					StringUtil.copyPartialMatches(partialCommand, commands, completions);
					Collections.sort(completions);
					return completions;
				} else if (args[0].equalsIgnoreCase("setperm")) {
					String partialCommand = args[0];
					Set<String> commands = config.getGroups().getValues(false).keySet();
					StringUtil.copyPartialMatches(partialCommand, commands, completions);
					Collections.sort(completions);
					return completions;
				}
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("setgroup") || args[0].equalsIgnoreCase("addsub")) {
					String partialCommand = args[0];
					Set<String> commands = config.getGroups().getValues(false).keySet();
					StringUtil.copyPartialMatches(partialCommand, commands, completions);
					Collections.sort(completions);
					return completions;
				} else if (args[0].equalsIgnoreCase("setwgroup")) {
					String partialCommand = args[0];
					Set<String> commands = config.getGroups().getValues(false).keySet();
					StringUtil.copyPartialMatches(partialCommand, commands, completions);
					Collections.sort(completions);
					return completions;
				} else if (args[0].equalsIgnoreCase("setprefix")) {
					l.add("<Prefix...>");
				} else if (args[0].equalsIgnoreCase("setsuffix")) {
					l.add("<Suffix...>");
				} else if (args[0].equalsIgnoreCase("setperm")) {
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
					se.sendMessage(pre + "Command Help:");
					se.sendMessage("/" + label + " setgroup <Player> <Group> §8§l- §7Set the group of a player");
					//se.sendMessage("/" + label + " setwgroup <Player> <Group> <Worldname> §8§l- §7Set the group of a player for a world");
					se.sendMessage("/" + label + " creategroup <Group> §8§l- §7Creates a group");
					se.sendMessage("/" + label + " removegroup <Group> §8§l- §7Removes a group");
					se.sendMessage("/" + label + " setprefix <Group> <Prefix...> §8§l- §7Sets the prefix of a group");
					se.sendMessage("/" + label + " setsuffix <Group> <Prefix...> §8§l- §7Sets the suffix of a group");
					se.sendMessage("/" + label + " setperm <Group> <Permission> §8§l- §7Toggle a permissions of a group");
					se.sendMessage("/" + label + " listgroup <Player> §8§l- §7Lists every group of a player");
					se.sendMessage("/" + label + " addsub <GroupFrom> <GroupTo> §8§l- §7Copies the permissions from GroupFrom to GroupTo");
					se.sendMessage("/" + label + " getgroup <Group> §8§l- §7Lists the permissions, the suffix and the prefix");
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (!se.hasPermission("kp.reload")) {
						se.sendMessage(noPerm());
						return true;
					}
					reloadConfig();
					se.sendMessage("§6KevsPermissions§8> §aReloaded config.");
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
					if (new PlayerGroup(args[1]).isValid()) {
						se.sendMessage(pre + "The group \"" + args[1] + "\" already exists.");
						return true;
					}
					config.getGroups().set(args[1] + ".prefix", "your new prefix");
					config.getGroups().set(args[1] + ".suffix", "your new suffix");
					List<String> permissions = new ArrayList<>();
					permissions.add("your.new.permission");
					config.getGroups().set(args[1] + ".permissions", permissions);
					permissions = new ArrayList<>();
					permissions.add("your-new-inherit");
					config.getGroups().set(args[1] + ".inherits", permissions);
					config.saveGroups();
					se.sendMessage(pre + "§2Group §7\"" + args[1] + "\"§2 successfully created!");
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (p.hasPermission("kp.creategroup") || p.isOp()) {
							if (!p.getName().equals(se.getName())) {
								p.sendMessage(se.getName() + " created group §2" + args[1]);
							}
						}
					}
				} else if (args[0].equalsIgnoreCase("removegroup")) {
					if (!se.hasPermission("kp.removegroup")) {
						se.sendMessage(noPerm());
						return true;
					}
					if (!new PlayerGroup(args[1]).isValid()) {
						se.sendMessage(pre + "The group \"" + args[1] + "\" does not exist. :(");
						return true;
					}
					config.getGroups().set(args[1], null);
					config.saveGroups();
					se.sendMessage(pre + "§2Group §7\"" + args[1] + "\"§2 successfully removed!");
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (p.hasPermission("kp.creategroup") || p.isOp()) {
							if (!p.getName().equals(se.getName())) {
								p.sendMessage(se.getName() + " removed group §2" + args[1]);							
							}
						}
					}
				} else if (args[0].equalsIgnoreCase("getgroup")) {
					if (!se.hasPermission("kp.getgroup")) {
						se.sendMessage(noPerm());
						return true;
					}
					if (!new PlayerGroup(args[1]).isValid()) {
						se.sendMessage(pre + "The group \"" + args[1] + "\" does not exist. :(");
						return true;
					}
					
					PlayerGroup group = new PlayerGroup(args[1]);
					se.sendMessage("§eGroup§8: §7" + group.getName());
					se.sendMessage("  §ePrefix§8: §7" + group.getPrefix() + " §8(§r" + group.getPrefix().replace("&", "§") + "§8)");
					se.sendMessage("  §eSuffix§8: §7" + group.getSuffix() + " §8(§r" + group.getSuffix().replace("&", "§") + "§8)");
					se.sendMessage("  §ePermissions§8:");
					for (String perm : group.getPermissions(false)) {
						se.sendMessage("    §7- §a" + perm);
					}
					se.sendMessage("  §eInherits§8:");
					for (String perm : group.getPermissions(true)) {
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
					se.sendMessage(pre + "§3Group(s) of " + args[1] + ":");
					se.sendMessage("" + config.getPlayerGroups(Bukkit.getOfflinePlayer(args[1]).getUniqueId()));
					for (PlayerGroup group : config.getPlayerGroups(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
						if (group != null) {
							se.sendMessage("  §eGroup§8: §7" + group.getName());
							se.sendMessage("    §ePrefix§8: §7" + group.getPrefix() + " §8(§r" + group.getPrefix().replace("&", "§") + "§8)");
							se.sendMessage("    §eSuffix§8: §7" + group.getSuffix() + " §8(§r" + group.getSuffix().replace("&", "§") + "§8)");
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
				if (config.getGroup(args[1]) == null) {
					se.sendMessage(pre + "The group \"" + args[1] + "\" doesn't exist. :(");
					return true;
				}
				String s = "";
				for (int i = 2; i < args.length; i++) {
					s += args[i] + " ";
				}
				s = s.substring(0, s.length()-1);
				config.getGroups().set(args[1] + ".prefix", s);
				config.saveGroups();
				se.sendMessage(pre + "Prefix of " + args[1] + " changed to " + s);
				return true;
			} else if (args.length>=3 && args[0].equalsIgnoreCase("setsuffix")) {
				if (!se.hasPermission("kp.setsuffix")) {
					se.sendMessage(noPerm());
					return true;
				}
				if (config.getGroup(args[1]) == null) {
					se.sendMessage(pre + "The group \"" + args[1] + "\" doesn't exist. :(");
					return true;
				}
				String s = "";
				for (int i = 2; i < args.length; i++) {
					s += args[i] + " ";
				}
				s = s.substring(0, s.length()-1);
				config.getGroups().set(args[1] + ".suffix", s);
				config.saveGroups();
				se.sendMessage(pre + "Suffix of " + args[1] + " changed to " + s);
				return true;
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("setgroup")) {
					if (!se.hasPermission("kp.setgroup")) {
						se.sendMessage(noPerm());
						return true;
					}
					if (config.getGroup(args[2]) == null) {
						se.sendMessage(pre + "The group \"" + args[2] + "\" doesn't exist. :(");
						return true;
					}
					List<String> groups = config.getPlayers().getStringList(Bukkit.getOfflinePlayer(args[1]).getUniqueId() + ".global.group");
					if (groups.contains(args[2])) {
						se.sendMessage(pre + "Removed group " + args[2] + " from " + args[1]);
						groups.remove(args[2]);
					} else {
						se.sendMessage(pre + "Added group " + args[2] + " to " + args[1]);
						groups.add(args[2]);
					}
					config.getPlayers().set(Bukkit.getOfflinePlayer(args[1]).getUniqueId() + ".global.group", groups);
					config.savePlayers();
					if (Bukkit.getOfflinePlayer(args[1]).isOnline()) {
						onJoin(new PlayerJoinEvent(Bukkit.getPlayer(args[1]), ""));
					}
				} else if (args[0].equalsIgnoreCase("setperm")) {
					if (!se.hasPermission("kp.setperm")) {
						se.sendMessage(noPerm());
						return true;
					}
					List<String> s = config.getGroup(args[1]).getPermissions(false);
					if (s.contains(args[2])) {
						s.remove(args[2]);
						se.sendMessage(pre + "Removed permission §a" + args[2] + "§7 from group " + args[1] + "!");
					} else {
						s.add(args[2]);
						se.sendMessage(pre + "Added permission §a" + args[2] + "§7 to group " + args[1] + "!");
					}
					config.getGroups().set(args[1] + ".permissions", s);
					config.saveGroups();
					for (Player p : Bukkit.getOnlinePlayers()) {
						for (PlayerGroup gru : config.getPlayerGroups(p.getUniqueId())) {
							if (gru.getName().equalsIgnoreCase(args[0])) {
								onJoin(new PlayerJoinEvent(p, ""));
							}
						}
					}
				} else if (args[0].equalsIgnoreCase("addsub")) {
					if (!se.hasPermission("kp.addsub")) {
						se.sendMessage(noPerm());
						return true;
					}
					
					PlayerGroup groupFrom = new PlayerGroup(args[1]);
					PlayerGroup groupTo = new PlayerGroup(args[2]);
					if (groupFrom.getName().equals(groupTo)) {
						se.sendMessage(pre + "§cThat groups are equals...");
						return true;
					}
					if (!groupFrom.isValid()) {
						se.sendMessage(pre + "§cThe group " + groupFrom.getName() + " is not valid.");
						return true;
					}
					if (!groupTo.isValid()) {
						se.sendMessage(pre + "§cThe group " + groupTo.getName() + " is not valid.");
						return true;
					}
					
					List<String> permsFrom = groupFrom.getPermissions(false);
					List<String> permsTo = groupTo.getPermissions(false);
					int i = 0;
					for (String addto : permsFrom) {
						if (!permsTo.contains(addto)) {
							permsTo.add(addto);
							i++;
						}
					}
					config.getGroups().set(args[2] + ".permissions", permsTo);
					config.saveGroups();
					se.sendMessage(pre + "Added every permission from group " + args[1] + " to group " + args[2] + ". Updated " + i + " permissions.");
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
	
	private String noPerm() {
		return new Random().nextBoolean() ? "§6KevsPermissions §8> §cYou don't have the permission(s) to do that." : "§6KevsPermissions §8> §cYou lack the permission(s) to do that.";
	}

	@SuppressWarnings("null")
	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (atts.containsKey(e.getPlayer().getUniqueId())) {
					disablePermission(atts.get(e.getPlayer().getUniqueId()));
					atts.remove(e.getPlayer().getUniqueId());
				}
				PermissionAttachment at = e.getPlayer().addAttachment(KevsPermissions.getPlugin(KevsPermissions.class), 1728000);
				for (PlayerGroup group : config.getPlayerGroups(e.getPlayer().getUniqueId())) {
					try {
						if (group != null || group.getPermissions(false) == null) {
							for (String perm : group.getPermissions(false)) {
								at.setPermission(perm.startsWith("-") ? perm.substring(1, perm.length()) : perm, perm.startsWith("-") ? false : true);
							}
							if (group.getWorldPerms(e.getPlayer().getWorld().getName()) != null) {
								for (String perm : group.getWorldPerms(e.getPlayer().getWorld().getName())) {
									if (!at.getPermissions().containsKey(perm.startsWith("-") ? perm.substring(1, perm.length()) : perm)) {
										at.setPermission(perm.startsWith("-") ? perm.substring(1, perm.length()) : perm, perm.startsWith("-") ? false : true);
									}
								}
							}
						}
					} catch (Exception e2) {}
				}
				if (config.getPlayers().get(e.getPlayer().getUniqueId() + ".permissions") != null) {
					List<String> permsToo = config.getPlayers().getStringList(e.getPlayer().getUniqueId() + ".permissions");
					for (String perm : permsToo) {
						if (!at.getPermissions().containsKey(perm.startsWith("-") ? perm.substring(1, perm.length()) : perm)) {
							at.setPermission(perm.startsWith("-") ? perm.substring(1, perm.length()) : perm, perm.startsWith("-") ? false : true);
						}
					}
				}
				atts.put(e.getPlayer().getUniqueId(), at);
			}
		});
		t.start();
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (atts.containsKey(e.getPlayer().getUniqueId())) {
			disablePermission(atts.get(e.getPlayer().getUniqueId()));
			atts.remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		onJoin(new PlayerJoinEvent(e.getPlayer(), null));
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (config.getCfg().getBoolean("antibuild")) {
			if (!e.getPlayer().hasPermission("kp.antibuild")) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(noPerm());
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (config.getCfg().getBoolean("antibuild")) {
			if (!e.getPlayer().hasPermission("kp.antibuild")) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(noPerm());
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (config.getCfg().getBoolean("antibuild")) {
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
		if (config.getCfg().getBoolean("antibuild")) {
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
	public void on(PlayerShearEntityEvent e) {
		if (config.getCfg().getBoolean("antibuild")) {
			if (!e.getPlayer().hasPermission("kp.antibuild"))
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(PlayerJoinEvent e) {
		if (config.getCfg().getBoolean("antibuild")) {
			if (!e.getPlayer().hasPermission("kp.antibuild"))
				e.getPlayer().sendMessage("§6KevsPermissions §8> §cYou currently lack the permission §7\"§akp.antibuild§7\"§c!");
		}
	}
}
 