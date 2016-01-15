package kev575.permissions;

import java.util.List;

public class PlayerGroup {

	public List<String> permissions;
	
	private String name;

	private String prefix, suffix;
	
	public PlayerGroup(String name) {
		if (!KevsPermissions.config.getGroups().contains(name)) {
			name = "default";
		}
		this.name = name;
		permissions = KevsPermissions.config.getGroups().getStringList(name + ".permissions");
		prefix = KevsPermissions.config.getGroups().getString(name + ".prefix");
		suffix = KevsPermissions.config.getGroups().getString(name + ".suffix");
		if (prefix == null) prefix = "";
		if (suffix == null) suffix = "";
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getPermissions() {
		return permissions;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
}
