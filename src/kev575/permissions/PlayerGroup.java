package kev575.permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerGroup {

	private String name;

	public PlayerGroup(String name) {
		this.name = name;
	}
	
	public List<String> getWorldPerms(String world) {
		if (KevsPermissions.config.getGroups().get(name + ".worlds." + world) == null) {
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
	
	public boolean isValid() {
		return KevsPermissions.config.getGroups().getConfigurationSection(getName()) != null;
	}
	
	public List<String> getPermissions(boolean inherits) {
		if (!inherits) {
			return KevsPermissions.config.getGroups().getStringList(name + ".permissions");
		} else {
			List<String> perms = new ArrayList<String>();
			for (String n : KevsPermissions.config.getGroups().getStringList(name + ".inherits")) {
				PlayerGroup current = new PlayerGroup(n);
				if (!current.isValid() || current.getName().equals(getName()))
					continue;
				for (String p : current.getPermissions(false)) {
					if (!perms.contains(p))
						perms.add(p);
				}
			}
			return perms;
		}
	}
	
	public List<PlayerGroup> getInherits() {
		List<PlayerGroup> groups = new ArrayList<PlayerGroup>();
		for (String n : KevsPermissions.config.getGroups().getStringList(name + ".inherits")) {
			groups.add(new PlayerGroup(n));
		}
		return groups;
	}
	
	public String getPrefix() {
		return KevsPermissions.config.getGroups().getString(name + ".prefix");
	}
	
	public String getSuffix() {
		return KevsPermissions.config.getGroups().getString(name + ".suffix");
	}
	
	public void setPrefix(String prefix) {
		KevsPermissions.config.getGroups().set(name + ".prefix", prefix);
		KevsPermissions.config.saveGroups();
	}
	
	public void setSuffix(String suffix) {
		KevsPermissions.config.getGroups().set(name + ".suffix", suffix);
		KevsPermissions.config.saveGroups();
	}
}
