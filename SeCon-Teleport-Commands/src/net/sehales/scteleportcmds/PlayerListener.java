package net.sehales.scteleportcmds;

import net.sehales.secon.SeCon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {

	private TeleportCmdCollection tc;

	PlayerListener(TeleportCmdCollection tc) {
		this.tc = tc;
	}

	@EventHandler()
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (SeCon.getAPI().getSeConUtils().hasPermission(e.getEntity(), tc.getConf().getString("back.track-permission.death"), false))
			PositionTracker.update(e.getEntity().getName(), e.getEntity().getLocation());
	}

	@EventHandler()
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		RequestHandler.remove(e.getPlayer().getName());
		PositionTracker.remove(e.getPlayer().getName());
	}

	@EventHandler()
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if (SeCon.getAPI().getSeConUtils().hasPermission(e.getPlayer(), tc.getConf().getString("back.track-permission.tp"), false))
			PositionTracker.update(e.getPlayer().getName(), e.getPlayer().getLocation());
	}
}
