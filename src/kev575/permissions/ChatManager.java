package kev575.permissions;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import kev575.json.KevsPermsPlayer;

public class ChatManager implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		
		if (KevsPermissions.vaultChat != null)
			return;
		
		if (KevsPermissions.config.getConfig().isBoolean("enablemanagers")) {
			if (KevsPermissions.config.getConfig().getBoolean("enablemanagers") && KevsPermissions.config.getConfig().getBoolean("enablechatman")) {
				if (e.getPlayer().hasPermission("kp.chatcolor")) {
					e.setMessage(e.getMessage().replace("&", "§"));
				}
				e.setCancelled(true);
				KevsPermsPlayer player = KevsPermissions.config.getPlayer(e.getPlayer().getUniqueId());
				player.fix();
				if (player.getPrefix().equalsIgnoreCase("*")) {
					player.setPrefix(KevsPermissions.config.getGroup(player.getGroups().get(0)).getPrefix());
				}
				if (player.getSuffix().equalsIgnoreCase("*")) {
					player.setSuffix(KevsPermissions.config.getGroup(player.getGroups().get(0)).getSuffix());
				}
				Bukkit.broadcastMessage(
						KevsPermissions.config.getConfig().getString("chatman")
						.replace("%p", e.getPlayer().getName())
						.replace("%x", player.getPrefix())
						.replace("%s", player.getSuffix())
						.replace("&", "§")
						.replace("%m", e.getMessage()));
			}
		}
	}
	
}
