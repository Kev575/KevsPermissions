package kev575.obf;

import kev575.permissions.KevsPermissions;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

public final class PluginSetPerms {

	public static void a() {
		for (String sub : KevsPermissions.COMMANDS)
			Bukkit.getPluginManager().addPermission(new Permission("kp." + sub));
	}
	
	public static void b(String sub) {
		Bukkit.getPluginManager().addPermission(new Permission("kp." + sub));
	}

}
