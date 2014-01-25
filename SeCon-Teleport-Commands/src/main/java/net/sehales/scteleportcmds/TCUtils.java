
package net.sehales.scteleportcmds;

import net.sehales.secon.utils.MiscUtils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TCUtils {
    
    private TeleportCmdCollection tc;
    
    TCUtils(TeleportCmdCollection tc) {
        this.tc = tc;
    }
    
    public void teleport(Player player, Location target, TeleportCause cause) {
        GameMode gamemode = player.getGameMode();
        boolean fly = player.getAllowFlight();
        boolean flying = player.isFlying();
        player.teleport(target, cause);
        if (MiscUtils.hasPermission(player, tc.getConf().getString("teleport-world.permission.remember-gamemode"), false)) {
            player.setGameMode(gamemode);
        } else {
            player.setGameMode(GameMode.SURVIVAL);
        }
        if (MiscUtils.hasPermission(player, tc.getConf().getString("teleport-world.permission.remember-fly"), false)) {
            player.setAllowFlight(fly);
            player.setFlying(flying);
        } else {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }
}
