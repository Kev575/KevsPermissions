package kev575.permissions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public final class PermissionsManager {

	private PermissionsManager() {
	}
	
	@SuppressWarnings("deprecation")
	public static boolean createGroup(String name) {
		if (!validateName(name))
			return false;
		File dir = new File(PermissionsPlugin.getInstance().getDataFolder(), "groups");
		if (!dir.exists()) {
			try {
				dir.mkdirs();				
			} catch (Exception e) {
				return false;
			}
		}
		name = name.toLowerCase();
		File group = new File(dir, name + ".yml");
		try {
			if (group.createNewFile()) {
				FileWriter fw = new FileWriter(group);
				fw.append("# KevsPermissions GROUP " + name + System.lineSeparator());
				fw.append("# Created at " + new Date(System.currentTimeMillis()).toGMTString() + System.lineSeparator());
				fw.append("name: \"" + name + "\"" + System.lineSeparator()); // changes will result in reset of name
				fw.append("cversion: \"" + PermissionsPlugin.getInstance().getDescription().getVersion() + "\"" + System.lineSeparator());
				fw.append("prefix: \"\"" + System.lineSeparator()); // empty as default
				fw.append("suffix: \"\"" + System.lineSeparator()); // empty as default
				fw.append("permissions: " + System.lineSeparator());
				fw.append("  - \"test.permission\"" + System.lineSeparator());
				fw.close();
				return true;
			} else
				return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * Check whether a name is valid   
	 */
	public static boolean validateName(String name) {
		boolean alphanumeric = StringUtils.isAlphanumeric(name);
		return alphanumeric;
	}
	
}
