package kev575.permissions.commands;

import org.bukkit.command.CommandSender;

public class PlayerCommand extends PermissionsExecutor {

	public PlayerCommand() {
		super("player", null);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO fill the method body
	}

	@Override
	public String getPermission() {
		return "kp.player";
	}

}
