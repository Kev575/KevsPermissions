package kev575.permissions.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import kev575.permissions.PermissionsConstants;

/**
 * This is the management class for /kp.
 * @author Kev575
 */
public class PermissionsCommand implements CommandExecutor, TabCompleter {

	private static ArrayList<PermissionsExecutor> executors = new ArrayList<>();
	
	static {
		executors.add(new VersionCommand());
		executors.add(new PlayersCommand());
		executors.add(new GroupsCommand());
		executors.add(new PlayerCommand());
		executors.add(new GroupCommand());
	}
	
	/**
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		if (cmd.getName().equalsIgnoreCase("kp")) {
			if (args.length == 0) {
				new VersionCommand().execute(sender, null);
			} else {
				PermissionsExecutor e = null;
				for (PermissionsExecutor exec : executors) {
					if (exec.getSubcommand().equalsIgnoreCase(args[0])) {
						e = exec;
						break;
					}
				}
				if (e == null) {
					sender.sendMessage(PermissionsConstants.PREFIX + "§cSorry, but §e/" + alias + " " + args[0] + " §cdoes not exist.");
					return true;
				}
				if (e.getPermission() != null && !sender.hasPermission(e.getPermission())) {
					sender.sendMessage(PermissionsConstants.PREFIX + "§cSorry, but you do not have the permission for that.");
					return true;
				}
				args = (String[]) ArrayUtils.removeElement(args, args[0]);
				e.execute(sender, args);
			}
		}
		return true; // always true - who cares?
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] args) {
		if (arg1.getName().equalsIgnoreCase("kp")) {
			List<String> completions = new ArrayList<String>();
			if (args.length == 1) {
				String partialCommand = args[0];
				List<String> commands = new ArrayList<String>();
				for (PermissionsExecutor x : executors) {
					if (arg0.hasPermission(x.getPermission())) {
						commands.add(x.getSubcommand());
					}
				}
				if (commands.size() == 0) {
					arg0.sendMessage(PermissionsConstants.PREFIX + "§cYou do not have the permission to do that.");
					return new ArrayList<>();
				}
				StringUtil.copyPartialMatches(partialCommand, commands, completions);
				Collections.sort(completions);
				return completions;
			} else if (args.length >= 2) {
				for (PermissionsExecutor x : executors) {
					if (arg0.hasPermission(x.getPermission())) {
						return x.tabComplete(arg0, (String[]) ArrayUtils.remove(args, 0));
					}
				}
			}
		}
		return new ArrayList<>();
	}

}
