package net.sehales.scteleportcmds;

import net.sehales.secon.addon.SeConAddon;
import net.sehales.secon.annotations.SeConAddonHandler;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

@SeConAddonHandler()
public class TeleportCmdCollection extends SeConAddon {

	void addConfigNode(String path, Object value) {
		if (!configContains(path))
			getConfig().set(path, value);
	}

	boolean configContains(String path) {
		return getConfig().contains(path);
	}

	FileConfiguration getConf() {
		return getConfig();
	}

	private void initConfig() {
		addConfigNode("teleport-world.permission.remember-fly", "secon.remember.fly");
		addConfigNode("teleport-world.permission.remember-gamemode", "secon.remember.gamemode");
		saveConfig();
	}

	private void initLanguage() {
		addLanguageInfoNode("teleport.teleported-to-you", "<gold>You have teleported <green><player> <gold>to you");
		addLanguageInfoNode("teleport.teleported-p2p", "<gold>You have teleported <green><firstplayer> <gold>to <green><secondplayer>");
		addLanguageInfoNode("teleport.teleported-to-player", "<gold>You have teleported yourself to <green><player>");
		addLanguageInfoNode("teleport.teleported-to-world", "<gold>You have teleported yourself to <green><world>");
		addLanguageInfoNode("teleport.teleported-player-to-world", "<gold>You have teleported <green><player> <gold>to <blue><world>");
		addLanguageInfoNode("teleport.teleported-to-location", "<gold>You have teleported yourself to <green>x: <x><grey>, <green>y: <y><grey>, <green>z:<z><gold> in <green>world: <world>");
		addLanguageInfoNode("teleport.world-not-found", "<red>Unknown world: '<blue><world><gold>'");
	}

	@Override
	protected void onDisable() {

	}

	@Override
	protected boolean onEnable(Plugin secon) {
		initConfig();
		initLanguage();
		TCUtils utils = new TCUtils(this);
		registerCommands(new TeleportCommands(this, utils));
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
