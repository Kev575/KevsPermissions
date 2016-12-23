package kev575.permissions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import kev575.permissions.io.PermissionsGroup;

public final class PermissionsManager {

	private static final ArrayList<PermissionsGroup> groups = new ArrayList<>();
	private static final ArrayList<String> groupNames = new ArrayList<>();
	private PermissionsManager() {
	}
	
	/**
	 * just for initializing progress in {@link PermissionsPlugin}
	 */
	protected static void refresh() {
		File dir = new File(PermissionsPlugin.getInstance().getDataFolder(), "groups");
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				System.out.println("Failed creation of KevsPermissions/groups folder! (" + e.getLocalizedMessage() + ")");
				throw e;
			}
			System.out.println("Created KevsPermissions/groups folder (maybe first setup).");
		}
		groups.clear(); // clearing this for multiple inits / refreshs
		groupNames.clear();
		for (File f : dir.listFiles()) {
			if (!f.isDirectory()) {
				if (f.getName().endsWith(".yml")) {
					String str = f.getName().replace(".yml", "");
					if (groupNames.contains(str.toLowerCase())) {
						continue;
					}
					groupNames.add(str.toLowerCase());
					if (StringUtils.isAlphanumeric(str)) {
						try {
							YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
							PermissionsGroup g = new PermissionsGroup(str.toLowerCase(), config, new Runnable() {
								
								@Override
								public void run() {
									try {
										config.save(f);
									} catch (Exception e) {
										PermissionsPlugin.getInstance().getLogger().log(Level.WARNING, "Could not save config for group " + str);
									}
								}
							});
							groups.add(g);
						} catch (Exception e) {
							System.out.println("Failed creation of KevsPermissions/groups/" + str + ".yml! (" + e.getLocalizedMessage() + ")");
							return;
						}
					}
				}
			}
		}
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
				String separator = "\n";
				fw.append("# KevsPermissions GROUP " + name + separator);
				fw.append("# Created at " + new Date(System.currentTimeMillis()).toGMTString() + separator);
				fw.append("cversion: \"" + PermissionsPlugin.getInstance().getDescription().getVersion() + "\"" + separator);
				fw.append("prefix: \"\"" + separator); // empty as default
				fw.append("suffix: \"\"" + separator); // empty as default
				fw.append("permissions: " + separator);
				fw.append("  - \"test.permission\"" + separator); // just a demo
				fw.close();
				refresh();
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

	/**
	 * Get a group; if not exists returns <code><b>null</b></code>
	 * @param string group name
	 */
	public static PermissionsGroup getGroup(String string) {
		for (PermissionsGroup permissionsGroup : groups) {
			if (permissionsGroup.getName().equals(string.toLowerCase())) {
				return permissionsGroup;
			}
		}
		return null;
	}
	
}
