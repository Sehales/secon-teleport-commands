
package net.sehales.scteleportcmds;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Request {
    
    public static final int TYPE_TPHERE = 0;
    public static final int TYPE_TPTO   = 1;
    private int             type;
    private String          sender;
    private String          receiver;
    
    public Request(String receiver, String sender, int type) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
    }
    
    public Player getReceiver() {
        return Bukkit.getPlayer(receiver);
    }
    
    public Player getSender() {
        return Bukkit.getPlayer(sender);
    }
    
    public int getType() {
        return this.type;
    }
    
}
