package kev575.json;

import java.util.ArrayList;
import java.util.HashMap;

public class KevsPermsGroup {
	private ArrayList<String> inherits;
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
	public ArrayList<String> getInherits() {
		return inherits;
	}
	public void setInherits(ArrayList<String> groups) {
		this.inherits = groups;
	}
	public HashMap<String, ArrayList<String>> getPermissions() {
		return permissions;
	}
	public void setPermissions(HashMap<String, ArrayList<String>> permissions) {
		this.permissions = permissions;
	}
	
	public void fix() {
		if (inherits == null)
			inherits = new ArrayList<String>();
		if (permissions == null)
			permissions = new HashMap<String, ArrayList<String>>();
		if (prefix == null)
			prefix = "your-new-prefix";
		if (suffix == null)
			suffix = "your-new-suffix";
	}
}
