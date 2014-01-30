
package net.sehales.scteleportcmds;

import net.sehales.secon.SeCon;
import net.sehales.secon.command.CommandType;
import net.sehales.secon.command.MethodCommandHandler;
import net.sehales.secon.command.SeConCommand;
import net.sehales.secon.config.LanguageConfig;
import net.sehales.secon.utils.MiscUtils;
import net.sehales.secon.utils.chat.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TeleportCommands {
    
    private TCUtils               utils;
    private TeleportCmdCollection tc;
    private LanguageConfig        lang = SeCon.getInstance().getLang();
    
    TeleportCommands(TeleportCmdCollection tc, TCUtils utils) {
        this.tc = tc;
        this.utils = utils;
    }
    
    @MethodCommandHandler(name = "back", description = "<darkaqua>go back to your last teleport location or your deathpoint (depends on permissions)", usage = "<darkaqua>/back", permission = "secon.command.back", type = CommandType.PLAYER)
    public void onBackCmd(Player player, SeConCommand cmd, String[] args) {
        Location l = tc.getPositionTracker().getLastLocation(player.getName());
        if (l == null) {
            ChatUtils.sendFormattedMessage(player, tc.getLanguageNode("teleport.back-no-location"));
            return;
        }
        utils.teleport(player, l, TeleportCause.COMMAND);
    }
    
    @MethodCommandHandler(name = "teleport", description = "<darkaqua>teleport to another player or one player to another one", usage = "<darkaqua>/teleport [player] [secondplayer]", additionalPerms = "other:secon.command.teleport.other", permission = "secon.command.teleport", aliases = { "tp", "tp2p", "teleportplayertoplayer", "tpp2p" })
    public void onTeleportCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (args.length > 1) {
                if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p == null) {
                        ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                        return;
                    }
                    Player sp = Bukkit.getPlayer(args[1]);
                    if (sp == null) {
                        ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[1]));
                        return;
                    }
                    utils.teleport(p, sp.getLocation(), TeleportCause.COMMAND);
                    ChatUtils.sendFormattedMessage(sender, tc.getLanguageNode("teleport.teleported-p2p").replace("<firstplayer>", p.getName()).replace("<secondplayer>", sp.getName()));
                }
            } else if (sender instanceof Player) {
                Player p = ((Player) sender).getPlayer();
                Player sp = Bukkit.getPlayer(args[0]);
                if (sp == null) {
                    ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                    return;
                }
                utils.teleport(p, sp.getLocation(), TeleportCause.COMMAND);
                ChatUtils.sendFormattedMessage(p, tc.getLanguageNode("teleport.teleported-to-player").replace("<player>", sp.getName()));
            }
        } else {
            ChatUtils.sendFormattedMessage(sender, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @MethodCommandHandler(name = "teleporthere", description = "<darkaqua>teleport another player to you", usage = "<darkaqua>/teleporthere [player]", permission = "secon.command.teleporthere", aliases = { "tphere", "tph" }, type = CommandType.PLAYER)
    public void onTeleportHereCmd(Player sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                return;
            }
            utils.teleport(p, sender.getLocation(), TeleportCause.COMMAND);
            ChatUtils.sendFormattedMessage(sender, tc.getLanguageNode("teleport.teleported-to-you").replace("<player>", p.getName()));
        } else {
            ChatUtils.sendFormattedMessage(sender, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @MethodCommandHandler(name = "teleportrequest", description = "<darkaqua>send a teleport request to another player", usage = "<darkaqua>/teleportrequest [player]", permission = "secon.command.teleportrequest", type = CommandType.PLAYER, aliases = { "tpr" })
    public void onTeleportRequestCmd(Player player, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                ChatUtils.sendFormattedMessage(player, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                return;
            }
            
            tc.getRequestHandler().add(p.getName(), new Request(p.getName(), player.getName(), Request.TYPE_TPTO));
            ChatUtils.sendFormattedMessage(p, tc.getLanguageNode("teleport.tpr-request-msg").replace("<sender>", player.getName()));
            ChatUtils.sendFormattedMessage(player, tc.getLanguageNode("teleport.tpr-request-sender-msg").replace("<player>", p.getName()));
        } else {
            ChatUtils.sendFormattedMessage(player, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @MethodCommandHandler(name = "teleportrequesthere", description = "<darkaqua>send a teleport request to another player", usage = "<darkaqua>/teleportrequesthere [player]", permission = "secon.command.teleportrequesthere", type = CommandType.PLAYER, aliases = { "tprh" })
    public void onTeleportRequestHereCmd(Player player, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                ChatUtils.sendFormattedMessage(player, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                return;
            }
            
            tc.getRequestHandler().add(p.getName(), new Request(p.getName(), player.getName(), Request.TYPE_TPHERE));
            ChatUtils.sendFormattedMessage(p, tc.getLanguageNode("teleport.tprh-request-msg").replace("<sender>", player.getName()));
            ChatUtils.sendFormattedMessage(player, tc.getLanguageNode("teleport.tprh-request-sender-msg").replace("<player>", p.getName()));
        } else {
            ChatUtils.sendFormattedMessage(player, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @MethodCommandHandler(name = "teleportworld", description = "<darkaqua>teleport yourself or another player into another world", usage = "<darkaqua>/teleportworld [player] [world]", additionalPerms = "world:secon.command.teleportworld.world.<world>,other:secon.command.teleportworld.other", permission = "secon.command.teleportworld", aliases = { "tpw", "teleportw", "tpworld" })
    public void onTeleportWorldCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (args.length > 1) {
                if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p == null) {
                        ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                        return;
                    }
                    if (!MiscUtils.hasPermission(sender, cmd.getPermission("world").replace("<world>", args[1]), true)) {
                        return;
                    }
                    World w = Bukkit.getWorld(args[1]);
                    if (w == null) {
                        ChatUtils.sendFormattedMessage(sender, tc.getLanguageNode("teleport.world-not-found").replace("<world>", args[1]));
                        return;
                    }
                    utils.teleport(p, w.getSpawnLocation(), TeleportCause.COMMAND);
                    ChatUtils.sendFormattedMessage(sender, tc.getLanguageNode("teleport.teleported-player-to-world").replace("<player>", p.getName()).replace("<world>", w.getName()));
                }
            } else if (sender instanceof Player) {
                Player p = ((Player) sender).getPlayer();
                if (!MiscUtils.hasPermission(sender, cmd.getPermission("world").replace("<world>", args[0]), true)) {
                    return;
                }
                World w = Bukkit.getWorld(args[0]);
                if (w == null) {
                    ChatUtils.sendFormattedMessage(sender, tc.getLanguageNode("teleport.world-not-found").replace("<world>", args[0]));
                    return;
                }
                utils.teleport(p, w.getSpawnLocation(), TeleportCause.COMMAND);
                ChatUtils.sendFormattedMessage(p, tc.getLanguageNode("teleport.teleported-to-world").replace("<world>", w.getName()));
            }
        } else {
            ChatUtils.sendFormattedMessage(sender, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @MethodCommandHandler(name = "tpaccept", description = "<darkaqua>accept a teleport request", usage = "<darkaqua>/tpaccept", permission = "secon.command.tpaccept", type = CommandType.PLAYER, aliases = { "tpyes", "tpy" })
    public void onTPAcceptCmd(Player player, SeConCommand cmd, String[] args) {
        if (tc.getRequestHandler().hasRequest(player.getName())) {
            Request req = tc.getRequestHandler().getRequest(player.getName());
            if (req.getSender() != null) {
                if (req.getType() == Request.TYPE_TPHERE) {
                    utils.teleport(player, req.getSender().getLocation(), TeleportCause.COMMAND);
                } else {
                    utils.teleport(req.getSender(), player.getLocation(), TeleportCause.COMMAND);
                }
                ChatUtils.sendFormattedMessage(req.getReceiver(), tc.getLanguageNode("teleport.request-accepted-msg").replace("<sender>", player.getName()));
                ChatUtils.sendFormattedMessage(player, tc.getLanguageNode("teleport.request-accepted-sender-msg").replace("<player>", req.getReceiver().getName()));
            }
        }
        ChatUtils.sendFormattedMessage(player, tc.getLanguageNode("teleport.no-request-pending"));
    }
    
    @MethodCommandHandler(name = "tpdeny", description = "<darkaqua>deny a teleport request", usage = "<darkaqua>/tpdeny", permission = "secon.command.tpdeny", type = CommandType.PLAYER, aliases = { "tpno", "tpn" })
    public void onTPDenyCmd(Player player, SeConCommand cmd, String[] args) {
        if (tc.getRequestHandler().hasRequest(player.getName())) {
            Request req = tc.getRequestHandler().getRequest(player.getName());
            ChatUtils.sendFormattedMessage(req.getReceiver(), tc.getLanguageNode("teleport.request-denied-msg").replace("<sender>", player.getName()));
            ChatUtils.sendFormattedMessage(player, tc.getLanguageNode("teleport.request-denied-sender-msg").replace("<player>", req.getReceiver().getName()));
            tc.getRequestHandler().remove(player.getName());
        }
        ChatUtils.sendFormattedMessage(player, tc.getLanguageNode("teleport.no-request-pending"));
    }
    
    @MethodCommandHandler(name = "tploc", description = "<darkaqua>teleport to a given location", usage = "<darkaqua>/tploc [x] [y] [z] [world]", additionalPerms = "world:secon.command.tploc.world.<world>", permission = "secon.command.tploc", aliases = { "tppos" }, type = CommandType.PLAYER)
    public void onTpLocCmd(Player player, SeConCommand cmd, String[] args) {
        if (args.length > 2) {
            try {
                double x = Integer.parseInt(args[0]);
                double y = Integer.parseInt(args[1]);
                double z = Integer.parseInt(args[2]);
                World w = null;
                if (args.length > 3) {
                    if (!MiscUtils.hasPermission(player, cmd.getPermission("world").replace("<world>", args[3]), true)) {
                        return;
                    }
                    w = Bukkit.getWorld(args[3]);
                }
                if (w == null) {
                    w = player.getWorld();
                }
                utils.teleport(player, new Location(w, x, y, z), TeleportCause.COMMAND);
                ChatUtils.sendFormattedMessage(player, tc.getLanguageNode("teleport.teleported-to-location").replace("<x>", Double.toString(x)).replace("<y>", Double.toString(y)).replace("<z>", Double.toString(z)).replace("<world>", w.getName()));
            } catch (Exception e) {
                ChatUtils.sendFormattedMessage(player, lang.ARGUMENT_CANT_BE_NULL);
            }
        } else {
            ChatUtils.sendFormattedMessage(player, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
}
