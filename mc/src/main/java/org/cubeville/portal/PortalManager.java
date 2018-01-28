package org.cubeville.portal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PortalManager
{
    private Plugin plugin;
    private Integer taskId;
    private List<Portal> portals;
    private static PortalManager instance;
    
    public PortalManager(Plugin plugin) {
        this.plugin = plugin;
        this.instance = this;
        taskId = null;
        portals = (List<Portal>) plugin.getConfig().get("Portals");
        if(portals == null) {
            portals = new ArrayList<>();
        }
    }

    public void start() {
        if(taskId != null) plugin.getServer().getScheduler().cancelTask(taskId);
        
        Runnable runnable = new Runnable() {
                public void run() {
                    Collection<Player> players = (Collection<Player>) plugin.getServer().getOnlinePlayers();
                    for(Portal portal: portals) {
                        portal.cyclicTrigger(players);
                    }
                }
            };

        taskId = plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, 30, 30).getTaskId();
    }

    public void stop() {
        plugin.getServer().getScheduler().cancelTasks(plugin);
    }

    public void save() {
        plugin.getConfig().set("Portals", portals);
        plugin.saveConfig();
    }

    public void addPortal(Portal portal) {
        portals.add(portal);
        save();
    }

    public void deletePortal(String name) {
        portals.remove(getPortal(name));
        save();
    }

    public Portal getPortal(String name) {
        for(Portal portal: portals) {
            if(portal.getName().equals(name)) {
                return portal;
            }
        }
        return null;
    }

    public static PortalManager getInstance() {
        return instance;
    }

    public List<Portal> getPortals() {
        return portals;
    }

}
