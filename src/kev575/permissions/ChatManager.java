package kev575.permissions;

import kev575.yaml.KevsPermsGroup;
import kev575.yaml.KevsPermsPlayer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (KevsPermissions.manager.getPluginConfig().getBoolean("chatmanager")) {
			if (e.getPlayer().hasPermission("kp.chatcolor")) {
				e.setMessage(e.getMessage().replace("&", "§"));
			}
			e.setCancelled(true);
			KevsPermsPlayer player = new KevsPermsPlayer(KevsPermissions.manager.getPlayer(e.getPlayer().getUniqueId()));
			String prefix = player.getPrefix(), suffix = player.getSuffix();
			if (player.getPrefix().equalsIgnoreCase("*") && player.getGroups().size() >= 1) {
				prefix = new KevsPermsGroup(KevsPermissions.manager.getGroup(player.getGroups().get(0))).getPrefix();
			}
			if (player.getSuffix().equalsIgnoreCase("*") && player.getGroups().size() >= 1) {
				suffix = new KevsPermsGroup(KevsPermissions.manager.getGroup(player.getGroups().get(0))).getSuffix();
			}
			Bukkit.broadcastMessage(
					KevsPermissions.manager.getPluginConfig().getString("chatman")
					.replace("%p", e.getPlayer().getName())
					.replace("%x", prefix)
					.replace("%s", suffix)
					.replace("&", "§")
					.replace("%m", e.getMessage()));
		}
	}
}