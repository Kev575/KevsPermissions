package kev575.sql;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import kev575.permissions.KevsPermissions;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

public class KevSQL {

	@SuppressWarnings("resource")
	public static void get() {
		if (KevsPermissions.config.getSQLManager() == null)
			return;
		try {
			ResultSet s = KevsPermissions.config.getSQLManager().prepareStatement("SELECT * FROM `kevspermissions`").executeQuery();
			new PrintWriter(new File(KevsPermissions.getPlugin(KevsPermissions.class).getDataFolder(), "groups.yml")).write(s.getString("groups"));
			new PrintWriter(new File(KevsPermissions.getPlugin(KevsPermissions.class).getDataFolder(), "players.yml")).write(s.getString("players"));
			KevsPermissions.config.setGroups(YamlConfiguration.loadConfiguration(new File(KevsPermissions.getPlugin(KevsPermissions.class).getDataFolder(), "groups.yml")));
			KevsPermissions.config.setPlayers(YamlConfiguration.loadConfiguration(new File(KevsPermissions.getPlugin(KevsPermissions.class).getDataFolder(), "players.yml")));
		} catch (Exception e) { return; }
	}
	public static void save() {
		if (KevsPermissions.config.getSQLManager() == null)
			return;
		try {
			KevsPermissions.config.getSQLManager().prepareStatement("UPDATE table_name SET groups='" + FileUtils.readFileToString(new File(KevsPermissions.getPlugin(KevsPermissions.class).getDataFolder(), "groups.yml")) + "', players='" + FileUtils.readFileToString(new File(KevsPermissions.getPlugin(KevsPermissions.class).getDataFolder(), "players.yml")) + "'").executeUpdate();
		} catch (SQLException | IOException e) { return; }
	}
	
}
