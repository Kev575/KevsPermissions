package kev575.permissions.io;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class PermissionsGroup {

	private final FileConfiguration config;
	private final Runnable save;
	private final String name;
	
	public PermissionsGroup(String name, FileConfiguration config, Runnable save) {
		Validate.notNull(name);
		Validate.notNull(config, "config is null");
		Validate.notNull(save, "save is null");
		this.config = config;
		this.save = save;
		this.name = name;
	}
	
	/**
	 * Get the {@link FileConfiguration} of this group
	 */
	public FileConfiguration getConfig() {
		return config;
	}
	
	/**
	 * Can throw unexpected exceptions;
	 */
	public void save() {
		save.run();
	}

	/**
	 * Get the name of the group; would not be null
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get all permissions in  
	 */
	public List<Permission> getPermissions() {
		List<String> orig = getConfig().getStringList("permissions");
		List<Permission> fynal = new ArrayList<>();
		if (orig.isEmpty())
			return fynal;
		for (String string : orig) {
			if (string.equals("*")) {
				fynal.add(new WildcardPermission(PermissionDefault.TRUE));
				continue;
			}
			if (string.startsWith("-")) {
				fynal.add(new Permission(string.substring(1), PermissionDefault.FALSE));
			} else {
				fynal.add(new Permission(string, PermissionDefault.TRUE));
			}
		}
		return fynal;
	}
	
	/**
	 * Get the prefix of the group 
	 */
	public String getPrefix() {
		return getConfig().getString("prefix");
	}
	
	/**
	 * Get the suffix of the group 
	 */
	public String getSuffix() {
		return getConfig().getString("suffix");
	}
	
	/**
	 * The configuration version.<br>This will be used in further versions to convert config(s) if necessary. 
	 */
	public String getCVersion() {
		return getConfig().getString("cversion");
	}
}
