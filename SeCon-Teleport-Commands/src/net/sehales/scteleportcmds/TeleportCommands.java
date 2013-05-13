package net.sehales.scteleportcmds;

import net.sehales.secon.SeCon;
import net.sehales.secon.addon.SeConCommand;
import net.sehales.secon.annotations.SeConCommandHandler;
import net.sehales.secon.config.LanguageHelper;
import net.sehales.secon.enums.CommandType;
import net.sehales.secon.utils.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TeleportCommands {

	private ChatUtils             chat = SeCon.getAPI().getChatUtils();
	private TCUtils               utils;
	private TeleportCmdCollection tc;

	TeleportCommands(TeleportCmdCollection tc, TCUtils utils) {
		this.tc = tc;
		this.utils = utils;
	}

	@SeConCommandHandler(name = "teleport", help = "<darkaqua>teleport to another player or one player to another one;<darkaqua>usage: /teleport [player] [secondplayer]", additionalPerms = "other:secon.command.teleport.other", permission = "secon.command.teleport", aliases = "tp,tp2p,teleportplayertoplayer,tpp2p,tpptp")
	public void onTeleportCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (args.length > 1) {
				if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
					Player p = Bukkit.getPlayer(args[0]);
					if (p == null) {
						chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
						return;
					}
					Player sp = Bukkit.getPlayer(args[1]);
					if (sp == null) {
						chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[1]));
						return;
					}
					utils.teleport(p, sp.getLocation(), TeleportCause.COMMAND);
					chat.sendFormattedMessage(sender, tc.getLanguageInfoNode("teleport.teleported-p2p").replace("<firstplayer>", p.getName()).replace("<secondplayer>", sp.getName()));
				}
			} else if (sender instanceof Player) {
				Player p = ((Player) sender).getPlayer();
				Player sp = Bukkit.getPlayer(args[0]);
				if (sp == null) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
					return;
				}
				utils.teleport(p, sp.getLocation(), TeleportCause.COMMAND);
				chat.sendFormattedMessage(p, tc.getLanguageInfoNode("teleport.teleported-to-player").replace("<player>", sp.getName()));
			}
		} else
			chat.sendFormattedMessage(sender, LanguageHelper.INFO_WRONG_ARGUMENTS);
	}

	@SeConCommandHandler(name = "teleporthere", help = "<darkaqua>teleport another player to you;<darkaqua>usage: /teleporthere [player]", permission = "secon.command.teleporthere", aliases = "tphere,tph", type = CommandType.PLAYER)
	public void onTeleportHereCmd(Player sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
				return;
			}
			utils.teleport(p, sender.getLocation(), TeleportCause.COMMAND);
			chat.sendFormattedMessage(sender, tc.getLanguageInfoNode("teleport.teleported-to-you").replace("<player>", p.getName()));
		} else
			chat.sendFormattedMessage(sender, LanguageHelper.INFO_WRONG_ARGUMENTS);
	}

	@SeConCommandHandler(name = "teleportrequest", help = "<darkaqua>send a teleport request to another player;<darkaqua>usage: /teleportrequest [player]", permission = "secon.command.teleportrequest", type = CommandType.PLAYER, aliases = "tpr")
	public void onTeleportRequestCmd(Player player, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				chat.sendFormattedMessage(player, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
				return;
			}
			new Request(p.getName(), player.getName(), Request.TYPE_TPTO);
			chat.sendFormattedMessage(p, tc.getLanguageInfoNode("teleport.tpr-request-msg").replace("<sender>", player.getName()));
			chat.sendFormattedMessage(player, tc.getLanguageInfoNode("teleport.tpr-request-sender-msg").replace("<player>", p.getName()));
		} else
			chat.sendFormattedMessage(player, LanguageHelper.INFO_WRONG_ARGUMENTS);
	}

	@SeConCommandHandler(name = "teleportrequesthere", help = "<darkaqua>send a teleport request to another player;<darkaqua>usage: /teleportrequesthere [player]", permission = "secon.command.teleportrequesthere", type = CommandType.PLAYER, aliases = "tprh")
	public void onTeleportRequestHereCmd(Player player, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				chat.sendFormattedMessage(player, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
				return;
			}
			new Request(p.getName(), player.getName(), Request.TYPE_TPHERE);
			chat.sendFormattedMessage(p, tc.getLanguageInfoNode("teleport.tprh-request-msg").replace("<sender>", player.getName()));
			chat.sendFormattedMessage(player, tc.getLanguageInfoNode("teleport.tprh-request-sender-msg").replace("<player>", p.getName()));
		} else
			chat.sendFormattedMessage(player, LanguageHelper.INFO_WRONG_ARGUMENTS);
	}

	@SeConCommandHandler(name = "teleportworld", help = "<darkaqua>teleport yourself or another player into another world;<darkaqua>usage: /teleportworld [player] [world]", additionalPerms = "world:secon.command.teleportworld.world.<world>,other:secon.command.teleportworld.other", permission = "secon.command.teleportworld", aliases = "tpw,teleportw,tpworld")
	public void onTeleportWorldCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (args.length > 1) {
				if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
					Player p = Bukkit.getPlayer(args[0]);
					if (p == null) {
						chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
						return;
					}
					if (!SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("world").replace("<world>", args[1]), true))
						return;
					World w = Bukkit.getWorld(args[1]);
					if (w == null) {
						chat.sendFormattedMessage(sender, tc.getLanguageInfoNode("teleport.world-not-found").replace("<world>", args[1]));
						return;
					}
					utils.teleport(p, w.getSpawnLocation(), TeleportCause.COMMAND);
					chat.sendFormattedMessage(sender, tc.getLanguageInfoNode("teleport.teleported-player-to-world").replace("<player>", p.getName()).replace("<world>", w.getName()));
				}
			} else if (sender instanceof Player) {
				Player p = ((Player) sender).getPlayer();
				if (!SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("world").replace("<world>", args[0]), true))
					return;
				World w = Bukkit.getWorld(args[0]);
				if (w == null) {
					chat.sendFormattedMessage(sender, tc.getLanguageInfoNode("teleport.world-not-found").replace("<world>", args[0]));
					return;
				}
				utils.teleport(p, w.getSpawnLocation(), TeleportCause.COMMAND);
				chat.sendFormattedMessage(p, tc.getLanguageInfoNode("teleport.teleported-to-world").replace("<world>", w.getName()));
			}
		} else
			chat.sendFormattedMessage(sender, LanguageHelper.INFO_WRONG_ARGUMENTS);
	}

	@SeConCommandHandler(name = "tpaccept", help = "<darkaqua>accept a teleport request;<darkaqua>usage: /tpaccept", permission = "secon.command.tpaccept", type = CommandType.PLAYER, aliases = "tpyes,tpy")
	public void onTPAcceptCmd(Player player, SeConCommand cmd, String[] args) {
		if (RequestHandler.hasRequest(player.getName())) {
			Request req = RequestHandler.getRequest(player.getName());
			if (req.getSender() != null) {
				if (req.getType() == Request.TYPE_TPHERE)
					utils.teleport(player, req.getSender().getLocation(), TeleportCause.COMMAND);
				else
					utils.teleport(req.getSender(), player.getLocation(), TeleportCause.COMMAND);
				chat.sendFormattedMessage(req.getReceiver(), tc.getLanguageInfoNode("teleport.request-accepted-msg").replace("<sender>", player.getName()));
				chat.sendFormattedMessage(player, tc.getLanguageInfoNode("teleport.request-accepted-sender-msg").replace("<player>", req.getReceiver().getName()));
			}
		}
		chat.sendFormattedMessage(player, tc.getLanguageInfoNode("teleport.no-request-pending"));
	}

	@SeConCommandHandler(name = "tpdeny", help = "<darkaqua>deny a teleport request;<darkaqua>usage: /tpdeny", permission = "secon.command.tpdeny", type = CommandType.PLAYER, aliases = "tpno,tpn")
	public void onTPDenyCmd(Player player, SeConCommand cmd, String[] args) {
		if (RequestHandler.hasRequest(player.getName())) {
			Request req = RequestHandler.getRequest(player.getName());
			chat.sendFormattedMessage(req.getReceiver(), tc.getLanguageInfoNode("teleport.request-denied-msg").replace("<sender>", player.getName()));
			chat.sendFormattedMessage(player, tc.getLanguageInfoNode("teleport.request-denied-sender-msg").replace("<player>", req.getReceiver().getName()));
			RequestHandler.remove(player.getName());
		}
		chat.sendFormattedMessage(player, tc.getLanguageInfoNode("teleport.no-request-pending"));
	}

	@SeConCommandHandler(name = "tploc", help = "<darkaqua>teleport to a given location;<darkaqua>usage: /tploc [x] [y] [z] [world]", additionalPerms = "world:secon.command.tploc.world.<world>", permission = "secon.command.tploc", aliases = "tppos", type = CommandType.PLAYER)
	public void onTpLocCmd(Player player, SeConCommand cmd, String[] args) {
		if (args.length > 2)
			try {
				double x = Integer.parseInt(args[0]);
				double y = Integer.parseInt(args[1]);
				double z = Integer.parseInt(args[2]);
				World w = null;
				if (args.length > 3) {
					if (!SeCon.getAPI().getSeConUtils().hasPermission(player, cmd.getPermission("world").replace("<world>", args[3]), true))
						return;
					w = Bukkit.getWorld(args[3]);
				}
				if (w == null)
					w = player.getWorld();
				utils.teleport(player, new Location(w, x, y, z), TeleportCause.COMMAND);
				chat.sendFormattedMessage(
				        player,
				        tc.getLanguageInfoNode("teleport.teleported-to-location").replace("<x>", Double.toString(x)).replace("<y>", Double.toString(y)).replace("<z>", Double.toString(z))
				                .replace("<world>", w.getName()));
			} catch (Exception e) {
				chat.sendFormattedMessage(player, LanguageHelper.INFO_WRONG_ARGUMENTS);
			}
		else
			chat.sendFormattedMessage(player, LanguageHelper.INFO_WRONG_ARGUMENTS);
	}
}
