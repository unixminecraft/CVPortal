package org.cubeville.portal.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;

@SerializableAs("Random")
public class Random implements Action
{
    Map<String, Integer> portals;

    public Random() {
        portals = new HashMap<>();
    }

    public Random(Map<String, Object> config) {
        portals = (Map<String, Integer>) config.get("portals");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("portals", portals);
        return ret;
    }

    public void addPortal(String name, int weight) {
        portals.put(name, weight);
    }

    public boolean removePortal(String name) {
        return portals.remove(name) != null;
    }

    public int getPortalCount() {
        return portals.size();
    }
    
    public void execute(Player player) {
        PortalManager portalManager = PortalManager.getInstance();
        int totalWeight = 0;
        List<String> portalNames = new ArrayList<>(portals.keySet());
        for(String portalName: portalNames) {
            if(portalManager.getPortal(portalName) != null) {
                totalWeight += portals.get(portalName);
            }
        }
        int randomNumber = (new java.util.Random()).nextInt(totalWeight);
        int currentWeight = 0;
        for(String portalName: portalNames) {
            if(portalManager.getPortal(portalName) != null) {
                if(randomNumber >= currentWeight && randomNumber < currentWeight + portals.get(portalName)) {
                    portalManager.getPortal(portalName).executeActions(player);
                    break;
                }
                currentWeight += portals.get(portalName);
            }
        }
    }

    public String getLongInfo() {
        String ret = " - Random: ";
        boolean first = true;
        for(String portalName: portals.keySet()) {
            if(!first) ret += ", ";
            else first = false;
            ret += portalName + "(" + portals.get(portalName) + ")";
        }
        return ret;
    }

    public String getShortInfo() {
        return "RND";
    }

    public boolean isSingular() {
        return true;
    }
}
