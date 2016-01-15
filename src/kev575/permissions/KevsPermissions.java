package kev575.permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

public class KevsPermissions extends JavaPlugin implements Listener {

	public static ConfigManager config;
	public String pre;
	
	HashMap<UUID, PermissionAttachment> atts = new HashMap<UUID,PermissionAttachment>();
	
	@Override
	public void onEnable() {
		pre = "§8[§6KevsPermissions§8]§7 ";
		config = new ConfigManager(this);
		Bukkit.getPluginManager().registerEvents(this, this);
		for (Player p : Bukkit.getOnlinePlayers()) {
			onJoin(new PlayerJoinEvent(p, ""));
		}
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
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		ArrayList<String> l = new ArrayList<>();
		if (cmd.getName().equalsIgnoreCase("kevspermissions")) {
			if (args.length == 1) {
				l.add("help");
				l.add("setgroup");
				l.add("setwgroup");
				l.add("creategroup");
				l.add("removegroup");
				l.add("setprefix");
				l.add("setperm");
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("setgroup")) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						l.add(p.getName());
					}
				} else if (args[0].equalsIgnoreCase("setwgroup")) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						l.add(p.getName());
					}
				} else if (args[0].equalsIgnoreCase("removegroup")) {
					l.addAll(config.getGroups().getValues(false).keySet());
				} else if (args[0].equalsIgnoreCase("setprefix")) {
					l.addAll(config.getGroups().getValues(false).keySet());
				} else if (args[0].equalsIgnoreCase("setperm")) {
					l.addAll(config.getGroups().getValues(false).keySet());
				}
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("setgroup")) {
					l.addAll(config.getGroups().getValues(false).keySet());
				} else if (args[0].equalsIgnoreCase("setwgroup")) {
					l.addAll(config.getGroups().getValues(false).keySet());
				} else if (args[0].equalsIgnoreCase("setprefix")) {
					l.add("<Prefix...>");
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
					se.sendMessage("/" + label + " setwgroup <Player> <Group> <Worldname> §8§l- §7Set the group of a player for a world");
					se.sendMessage("/" + label + " creategroup <Group> §8§l- §7Creates a group");
					se.sendMessage("/" + label + " removegroup <Group> §8§l- §7Removes a group");
					se.sendMessage("/" + label + " setprefix <Group> <Prefix...> §8§l- §7Sets the prefix of a group");
					se.sendMessage("/" + label + " setperm <Group> <Permission>");
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
					try {
						if (config.getGroup(args[1]).getName().equalsIgnoreCase(args[1])) {
							se.sendMessage(pre + "The group \"" + args[1] + "\" already exists. :(");
							return true;
						}
					} catch (Exception e) {}
					config.getGroups().set(args[1] + ".prefix", "your new prefix");
					config.getGroups().set(args[1] + ".suffix", "your new suffix");
					List<String> permissions = new ArrayList<>();
					permissions.add("your.new.permission");
					config.getGroups().set(args[1] + ".permissions", permissions);
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
					if (config.getGroup(args[1]) == null) {
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
					List<String> s = config.getGroup(args[1]).getPermissions();
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
						for (PlayerGroup gru : config.getPlayersGroup(p.getUniqueId())) {
							if (gru.getName().equalsIgnoreCase(args[0])) {
								onJoin(new PlayerJoinEvent(p, ""));
							}
						}
					}
				} else {
					se.sendMessage(pre + "Use /" + label + " help");
				}
			} else if (args.length == 4) {
				if (args[0].equalsIgnoreCase("setwgroup")) {
					if (!se.hasPermission("kp.setwgroup")) {
						se.sendMessage(noPerm());
						return true;
					}
					config.getPlayers().set(Bukkit.getOfflinePlayer(args[1]).getUniqueId() + "." + args[3] + ".group", args[2]);
					config.savePlayers();
					se.sendMessage(pre + "Group of " + args[1] + " changed to " + args[2]);
					if (Bukkit.getOfflinePlayer(args[1]).isOnline()) {
						onJoin(new PlayerJoinEvent(Bukkit.getPlayer(args[1]), ""));
					}
				}
			} else {
				se.sendMessage(pre + "Use /" + label + " help");
			}
		}
		return true;
	}
	
	private String noPerm() {
		return pre + "§cYou don't have the permission(s) to do that.";
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (atts.containsKey(e.getPlayer().getUniqueId())) {
			disablePermission(atts.get(e.getPlayer().getUniqueId()));
			atts.remove(e.getPlayer().getUniqueId());
		}
		for (PlayerGroup group : config.getPlayersGroup(e.getPlayer().getUniqueId())) {
			PermissionAttachment at = e.getPlayer().addAttachment(this, 1728000);
			for (String perm : group.getPermissions()) {
				at.setPermission(perm, true);
			}
			atts.put(e.getPlayer().getUniqueId(), at);
		}
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
	
}
 