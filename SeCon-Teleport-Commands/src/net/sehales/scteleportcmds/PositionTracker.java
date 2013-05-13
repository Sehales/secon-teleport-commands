package net.sehales.scteleportcmds;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class PositionTracker {

	private static Map<String, Location> positions = new HashMap<String, Location>();

	public static Location getLastLocation(String playerName) {
		return positions.containsKey(playerName)? positions.get(playerName) : null;
	}

	public static void remove(String playerName) {
		positions.remove(playerName);
	}

	public static void update(String playerName, Location loc) {
		positions.put(playerName, loc);
	}
}
