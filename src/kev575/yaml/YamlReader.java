package kev575.yaml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;


public abstract class YamlReader {
	
	ConfigurationSection section;
	
	public YamlReader(ConfigurationSection sec) {
		section = sec;
	}

	public ConfigurationSection getSection() {
		return section;
	}
	
	public List<String> getPermissions() {
		return getSection().getStringList("permissions");
	}
	
	public List<String> getInherits() {
		return getSection().getStringList("inherits");
	}
	
	public String getPrefix() {
		return getSection().getString("prefix");
	}
	
	public String getSuffix() {
		return getSection().getString("suffix");
	}
	
	public void setPermissions(List<String> permissions) {
		getSection().set("permissions", permissions);
		saveConfig();
	}
	
	public void setInherits(List<String> permissions) {
		getSection().set("permissions", permissions);
		saveConfig();
	}
	
	public void setPrefix(String prefix) {
		getSection().set("prefix", prefix);
		saveConfig();
	}
	
	public void setSuffix(String suffix) {
		getSection().set("suffix", suffix);
		saveConfig();
	}
	
	public void create() {
		setPrefix("your new prefix");
		setSuffix("your new suffix");
		setPermissions(new ArrayList<String>(Arrays.asList("your.new.permission")));
		setInherits(new ArrayList<String>(Arrays.asList("your.new.inherit")));
	}
	
	public abstract void saveConfig();
	
	public boolean isNull() {
		return section == null;
	}
}
