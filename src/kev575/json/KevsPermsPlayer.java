package kev575.json;

import java.util.ArrayList;
import java.util.HashMap;

public class KevsPermsPlayer {
	private ArrayList<String> groups;
	private HashMap<String, ArrayList<String>> permissions;
	private String prefix, suffix;
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
	
	public void fix() {
		if (groups == null)
			groups = new ArrayList<String>();
		if (permissions == null)
			permissions = new HashMap<String, ArrayList<String>>();
		if (suffix == null)
			suffix = "new-suffix";
		if (prefix == null)
			prefix = "new-prefix";
	}
}