package kev575.permissions;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		
		if (KevsPermissions.vaultChat != null)
			return;
		
		if (KevsPermissions.config.getCfg().isBoolean("enablemanagers")) {
			if (KevsPermissions.config.getCfg().getBoolean("enablemanagers") && KevsPermissions.config.getCfg().getBoolean("enablechatman")) {
				if (e.getPlayer().hasPermission("kp.chatcolor")) {
					e.setMessage(e.getMessage().replace("&", "§"));
				}
				e.setCancelled(true);
				Bukkit.broadcastMessage(
						KevsPermissions.config.getCfg().getString("chatman")
						.replace("&", "§")
						.replace("%p", e.getPlayer().getName())
						.replace("%x", KevsPermissions.config.getPlayerGroups(e.getPlayer().getUniqueId()).get(0).getPrefix()
						.replace("&", "§"))
						.replace("%s", KevsPermissions.config.getPlayerGroups(e.getPlayer().getUniqueId()).get(0).getSuffix()
						.replace("&", "§"))
						.replace("%m", e.getMessage()));
			}
		}
	}
	
}
