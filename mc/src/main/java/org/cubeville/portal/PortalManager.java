package org.cubeville.portal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

public class PortalManager implements Listener
{
    private Plugin plugin;
    private Integer taskId;
    private List<Portal> portals;
    private static PortalManager instance;
    private Map<UUID, Portal> respawnPortals;
    private Set<UUID> ignoredPlayers;
    
    @SuppressWarnings("unchecked")
    public PortalManager(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
        taskId = null;
        portals = (List<Portal>) plugin.getConfig().get("Portals");
        if(portals == null) {
            portals = new ArrayList<>();
        }
        respawnPortals = new HashMap<>();
        ignoredPlayers = new HashSet<>();
    }

    public void ignorePlayer(UUID Player) {
        ignoredPlayers.add(Player);
    }

    public void unignorePlayer(UUID Player) {
        ignoredPlayers.remove(Player);
    }
    
    public void start() {
        if(taskId != null) plugin.getServer().getScheduler().cancelTask(taskId);
        
        Runnable runnable = new Runnable() {
                @SuppressWarnings("unchecked")
                public void run() {
                    Collection<Player> onlinePlayers = (Collection<Player>) plugin.getServer().getOnlinePlayers();
                    Collection<Player> players;
                    if(ignoredPlayers.size() == 0) {
                        players = onlinePlayers;
                    }
                    else {
                        List<Player> playerList = new ArrayList<>();
                        for(Player player: onlinePlayers) {
                            if(!ignoredPlayers.contains(player.getUniqueId())) {
                                playerList.add(player);
                            }
                        }
                        players = playerList;
                    }
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
    
    public List<Portal> getMatchingPortals(String search) {
        List<Portal> match = new ArrayList<Portal>();
        for(Portal portal: portals) {
            if(portal.getName().contains(search)) {
                match.add(portal);
            }
        }
        return match;
    }

    public static PortalManager getInstance() {
       return instance;
    }

    public List<Portal> getPortals() {
        return portals;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        for(Portal portal: portals) {
            if(portal.isDeathTriggered() && portal.isPlayerInPortal(player)) {
                System.out.println("Portal is a death triggered portal, saving player status");
                respawnPortals.put(player.getUniqueId(), portal);
                return;
            }
        }
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if(respawnPortals.containsKey(player.getUniqueId())) {
            Location loc = respawnPortals.get(player.getUniqueId()).getTeleportLocation();
            if(loc != null) event.setRespawnLocation(loc);
            respawnPortals.remove(player.getUniqueId());
        }
    }
    
}
