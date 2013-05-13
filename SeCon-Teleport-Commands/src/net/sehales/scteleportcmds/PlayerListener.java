package net.sehales.scteleportcmds;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	@EventHandler()
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		RequestHandler.remove(e.getPlayer().getName());
	}
}
