package kev575.permissions.io;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;

/**
 * @author Kev575
 */
public class PlayerPermissible extends PermissibleBase {
	
	public PlayerPermissible(Player p) {
		super(new ServerOperator() {
			@Override
			public void setOp(boolean op) {
				p.setOp(op);
			}
			
			@Override
			public boolean isOp() {
//				return Bukkit.getOperators().contains(p);
				return p.isOp();
			}
		});
	}
	
	@Override
	public boolean hasPermission(Permission perm) {
		for (PermissionAttachmentInfo pa : getEffectivePermissions()) {
			if (perm.getName().equalsIgnoreCase(pa.getPermission())) {
				return pa.getValue();
			}
		}
		return false;
	}

}
