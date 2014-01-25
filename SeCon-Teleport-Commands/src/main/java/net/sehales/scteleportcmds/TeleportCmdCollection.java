
package net.sehales.scteleportcmds;

import net.sehales.secon.addon.Addon;
import net.sehales.secon.addon.SeConAddonHandler;

import org.bukkit.configuration.file.FileConfiguration;

@SeConAddonHandler()
public class TeleportCmdCollection extends Addon {
    
    private RequestHandler  requestHandler;
    private PositionTracker posTracker;
    
    void addConfigNode(String path, Object value) {
        if (!configContains(path)) {
            getConfig().set(path, value);
        }
    }
    
    boolean configContains(String path) {
        return getConfig().contains(path);
    }
    
    FileConfiguration getConf() {
        return getConfig();
    }
    
    public PositionTracker getPositionTracker() {
        return posTracker;
    }
    
    public RequestHandler getRequestHandler() {
        return requestHandler;
    }
    
    private void initConfig() {
        addConfigNode("teleport-world.permission.remember-fly", "secon.remember.worldchange.fly");
        addConfigNode("teleport-world.permission.remember-gamemode", "secon.remember.worldchange.gamemode");
        addConfigNode("back.track-permission.death", "secon.command.back.death");
        addConfigNode("back.track-permission.tp", "secon.command.back.tp");
        saveConfig();
    }
    
    private void initLanguage() {
        addLanguageNode("teleport.teleported-to-you", "<gold>You have teleported <green><player> <gold>to you");
        addLanguageNode("teleport.teleported-p2p", "<gold>You have teleported <green><firstplayer> <gold>to <green><secondplayer>");
        addLanguageNode("teleport.teleported-to-player", "<gold>You have teleported yourself to <green><player>");
        addLanguageNode("teleport.teleported-to-world", "<gold>You have teleported yourself to <green><world>");
        addLanguageNode("teleport.teleported-player-to-world", "<gold>You have teleported <green><player> <gold>to <blue><world>");
        addLanguageNode("teleport.teleported-to-location", "<gold>You have teleported yourself to <green>x: <x><grey>, <green>y: <y><grey>, <green>z:<z><gold> in <green>world: <world>");
        addLanguageNode("teleport.world-not-found", "<red>Unknown world: <grey>'<green><world><grey>'");
        addLanguageNode("teleport.tpr-request-msg", "<green><sender> <gold>has made a teleport request (he to you). Accept with /tpaccept, deny with /tpdeny.");
        addLanguageNode("teleport.tpr-request-sender-msg", "<gold>You have sent a teleport request (you to him) to <green><player>");
        addLanguageNode("teleport.tprh-request-msg", "<green><sender> <gold>has made a teleport request (you to him). Accept with /tpaccept, deny with /tpdeny.");
        addLanguageNode("teleport.tprh-request-sender-msg", "<gold>You have sent a teleport request (he to you) to <green><player>");
        addLanguageNode("teleport.request-denied-msg", "<red>You teleport request has been denied by <green><sender>");
        addLanguageNode("teleport.request-accepted-msg", "<gold>Your teleport request has been accepted by <green><sender>");
        addLanguageNode("teleport.request-denied-sender-msg", "<gold>You have denied the teleport request of <green><player>");
        addLanguageNode("teleport.request-accepted-sender-msg", "<gold>You have accepted the teleport request of <green><player>");
        addLanguageNode("teleport.no-request-pending", "<red>You have no pending request");
        addLanguageNode("teleport.back-no-location", "<red>There is no location, you can go back to");
    }
    
    @Override
    protected boolean onEnable() {
        initConfig();
        initLanguage();
        requestHandler = new RequestHandler();
        posTracker = new PositionTracker();
        TCUtils utils = new TCUtils(this);
        registerListener(new PlayerListener(this));
        registerCommandsFromObject(new TeleportCommands(this, utils));
        return true;
    }
    
    void reloadConf() {
        reloadConfig();
    }
    
    void saveConf() {
        saveConfig();
    }
    
    void setConfigNode(String path, Object value) {
        getConfig().set(path, value);
    }
    
}
