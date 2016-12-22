package kev575.permissions.commands;

import org.bukkit.command.CommandSender;

public class GroupsCommand extends PermissionsExecutor {

	public GroupsCommand() {
		super("groups", null);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO fill the method body
	}

	@Override
	public String getPermission() {
		return "kp.groups";
	}

}
