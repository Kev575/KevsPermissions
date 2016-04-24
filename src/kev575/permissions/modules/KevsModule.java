package kev575.permissions.modules;


import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class KevsModule extends JavaPlugin {
	private static final ArrayList<KevsModule> modules = new ArrayList<KevsModule>();
	
	public static ArrayList<KevsModule> getModules() {
		return modules;
	}
	@Override
	public void onEnable() {
		modules.add(this);
	}
}
