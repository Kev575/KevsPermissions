package kev575.permissions.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

public class GroupCommand extends PermissionsExecutor {

	public GroupCommand() {
		super("group", null);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO fill the method body
	}

	@Override
	public String getPermission() {
		return "kp.group";
	}

	@Override
	public List<String> tabComplete(CommandSender arg0, Object[] remove) {
		// TODO fill the method body
		return null;
	}

}
