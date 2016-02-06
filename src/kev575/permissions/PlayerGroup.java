package kev575.permissions;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

public class PlayerGroup {

	public List<String> permissions;
	private List<String> inherits;
	
	private String name;

	private String prefix, suffix;
	
	public PlayerGroup(String name) {
		if (KevsPermissions.config.getGroups().getConfigurationSection(name) == null) {
			name = KevsPermissions.config.getCfg().getString("default");
			return;
		}
		this.name = name;
		if (KevsPermissions.config.getGroups().get(name + ".permissions") == null) {
			KevsPermissions.config.getGroups().set(name + ".permissions", Arrays.asList("your.new.permission"));
		}
		if (KevsPermissions.config.getGroups().get(name + ".inherits") == null) {
			KevsPermissions.config.getGroups().set(name + ".inherits", Arrays.asList("group-inherit"));
		}
		inherits = KevsPermissions.config.getGroups().getStringList(name + ".inherits");
		permissions = KevsPermissions.config.getGroups().getStringList(name + ".permissions");
		if (permissions.contains("*")) {
			for (Permission perm : Bukkit.getPluginManager().getPermissions()) {
				permissions.add(perm.getName());
			}
		}
		for (String inhr : inherits) {
			PlayerGroup g = new PlayerGroup(inhr);
			if (g == null || g.getPermissions() == null || g.getPermissions().get(0).equalsIgnoreCase(""))
				continue;
			for (String per : g.getPermissions()) {
				if (permissions.contains(per))
					continue;
				permissions.add(per);
			}
		}
		prefix = (String) KevsPermissions.config.getGroups().get(name + ".prefix");
		suffix = (String) KevsPermissions.config.getGroups().get(name + ".suffix");
		if (prefix == null) { KevsPermissions.config.getGroups().set(name + ".prefix", "prefix"); prefix = ""; }
		if (suffix == null) { KevsPermissions.config.getGroups().set(name + ".suffix", "suffix"); suffix = ""; }
		
		
		
		KevsPermissions.config.saveGroups();
		
	}
	
	public List<String> getWorldPerms(String world) {
		if (KevsPermissions.config.getGroups().getStringList(name + ".worlds." + world) == null) {
			KevsPermissions.config.getGroups().set(name + ".worlds." + world, Arrays.asList("your-world-here"));
			KevsPermissions.config.saveGroups();
			return Arrays.asList("");
		}
		List<String> list = KevsPermissions.config.getGroups().getStringList(name + ".worlds." + world);
		return list;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getInherits() {
		return inherits;
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
