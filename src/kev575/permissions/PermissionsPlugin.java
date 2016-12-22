package kev575.permissions;

import org.bukkit.plugin.java.JavaPlugin;

import kev575.permissions.commands.PermissionsCommand;

/**
 * This is the base class for KevsPermissions.
 * 
 * @author Kev575
 */
public class PermissionsPlugin extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getCommand("kp").setExecutor(new PermissionsCommand());
	}

	public static PermissionsPlugin getInstance() {
		return PermissionsPlugin.getPlugin(PermissionsPlugin.class);
	}
}
