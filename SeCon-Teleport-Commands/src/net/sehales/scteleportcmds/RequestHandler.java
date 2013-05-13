package net.sehales.scteleportcmds;

import java.util.HashMap;
import java.util.Map;

public class RequestHandler {

	private static Map<String, Request> tpRequestMap = new HashMap<String, Request>();

	static void add(String name, Request request) {
		tpRequestMap.put(name, request);
	}

	public static Request getRequest(String name) {
		return hasRequest(name)? tpRequestMap.get(name) : null;
	}

	public static boolean hasRequest(String name) {
		return tpRequestMap.containsKey(name);
	}

	public static void remove(String name) {
		if (hasRequest(name))
			tpRequestMap.remove(name);
	}
}
