package kev575.obf;

import kev575.permissions.KevsPermissions;

import org.bukkit.Bukkit;

public final class PluginSetInPerms {

	public static void a() {
		for (String sub : KevsPermissions.COMMANDS)
			Bukkit.getPluginManager().addPermission(new org.bukkit.permissions.Permission("kp." + sub));
	}

}
