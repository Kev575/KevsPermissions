package kev575.permissions.io;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class WildcardPermission extends Permission {

	public WildcardPermission(PermissionDefault defaultValue) {
		super("*", defaultValue);
	}
	
	@Override
	public String getDescription() {
		return "The wildcard grants every permission on the bukkit / spigot system.";
	}
}
