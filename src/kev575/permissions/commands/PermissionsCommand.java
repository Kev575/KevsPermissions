package kev575.permissions.commands;

import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import kev575.permissions.PermissionsConstants;

/**
 * This is the management class for /kp.
 * @author Kev575
 */
public class PermissionsCommand implements CommandExecutor {

	private static ArrayList<PermissionsExecutor> executors = new ArrayList<>();
	
	static {
		executors.add(new VersionCommand());
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
				args = (String[]) ArrayUtils.removeElement(args, args[0]);
				e.execute(sender, args);
			}
		}
		return true; // always true - who cares?
	}

}
