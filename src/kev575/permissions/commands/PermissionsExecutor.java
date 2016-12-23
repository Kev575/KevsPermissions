package kev575.permissions.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

/**
 * @author Kev575
 */
public abstract class PermissionsExecutor {

	private final String subCommand;
	private final ArrayList<String> aliases;
	
	/**
	 * @param subCommand the subcommand, for example <code>reload</code>
	 * @param aliases the aliases, ignored if null
	 */
	public PermissionsExecutor(String subCommand, ArrayList<String> aliases) {
		this.subCommand = subCommand;
		this.aliases = aliases;
	}
	
	public String getSubcommand() {
		return subCommand;
	}
	
	public ArrayList<String> getAliases() {
		if (aliases == null)
			return new ArrayList<>();
		return aliases;
	}
	
	public abstract void execute(CommandSender sender, String[] args);
	public abstract String getPermission();
	public abstract List<String> tabComplete(CommandSender arg0, Object[] remove);
}
