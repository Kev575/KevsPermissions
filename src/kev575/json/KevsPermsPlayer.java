package kev575.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import kev575.permissions.KevsPermissions;

import org.bukkit.Bukkit;

public class KevsPermsPlayer {
	private ArrayList<String> groups;
	private HashMap<String, ArrayList<String>> permissions;
	private String prefix, suffix, lastname;
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public ArrayList<String> getGroups() {
		return groups;
	}
	public void setGroups(ArrayList<String> groups) {
		this.groups = groups;
	}
	public HashMap<String, ArrayList<String>> getPermissions() {
		return permissions;
	}
	public void setPermissions(HashMap<String, ArrayList<String>> permissions) {
		this.permissions = permissions;
	}
	public String getLastname() {
		return lastname;
	}
	
	public void fix(UUID uniqueId) {
		if (groups == null) {
			ArrayList<String> str = new ArrayList<String>();
			str.add(KevsPermissions.config.getConfig().getString("default"));
			groups = str;
		}
		if (permissions == null)
			permissions = new HashMap<String, ArrayList<String>>();
		if (suffix == null)
			suffix = "*";
		if (prefix == null)
			prefix = "*";
		lastname = Bukkit.getOfflinePlayer(uniqueId).getName();
	}
}