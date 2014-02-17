
package net.sehales.scteleportcmds;

import net.sehales.secon.utils.MiscUtils;

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
        if (MiscUtils.hasPermission(e.getEntity(), tc.getConf().getString("back.track-permission.death"), false)) {
            tc.getPositionTracker().update(e.getEntity().getName(), e.getEntity().getLocation());
        }
    }
    
    @EventHandler()
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        tc.getRequestHandler().remove(e.getPlayer().getName());
        // PositionTracker.remove(e.getPlayer().getName());
    }
    
    @EventHandler()
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (MiscUtils.hasPermission(e.getPlayer(), tc.getConf().getString("back.track-permission.tp"), false)) {
            tc.getPositionTracker().update(e.getPlayer().getName(), e.getFrom());
        }
    }
}
