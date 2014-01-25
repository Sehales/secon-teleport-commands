
package net.sehales.scteleportcmds;

import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    
    private Map<String, Request> tpRequestMap = new HashMap<String, Request>();
    
    void add(String name, Request request) {
        tpRequestMap.put(name, request);
    }
    
    public Request getRequest(String name) {
        return hasRequest(name) ? tpRequestMap.get(name) : null;
    }
    
    public boolean hasRequest(String name) {
        return tpRequestMap.containsKey(name);
    }
    
    public void remove(String name) {
        if (hasRequest(name)) {
            tpRequestMap.remove(name);
        }
    }
}
