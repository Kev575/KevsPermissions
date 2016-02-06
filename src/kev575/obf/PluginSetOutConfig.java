package kev575.obf;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class PluginSetOutConfig {

	public static void a(FileConfiguration arg0) {
		if (arg0.get("antibuild") == null) {
			arg0.set("antibuild", false);
		}
		if (arg0.get("default") == null) {
			arg0.set("default", "default");
		}
		if (arg0.get("chatman") == null) {
			arg0.set("default", "&7%x%p&8: &7%m");
		}
		if (arg0.get("scoreboardmanager") == null) {
			arg0.set("scoreboardmanager", false);
		}
	}

	public static void b(Plugin arg0) {
		arg0.saveConfig();
	}

}
