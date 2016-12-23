package kev575.permissions.io;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;

public class PermissionsUtils {

	/**
	 * Set the permissible of a player
	 * @param p the player
	 * @param base the permissible base
	 * @return true if succeeded
	 */
	public static boolean setPermissible(Player p, PermissibleBase base) {
		try {
			Field field = p.getClass().getDeclaredField("perm");
			field.setAccessible(true);
			Field modifiersField = field.getClass().getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(p, base);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
