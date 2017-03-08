package org.cubeville.portal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import org.cubeville.cvipc.IPCInterface;

public class LoginTeleporter implements Listener, IPCInterface
{
    Map<UUID, String> loginTargets;
    PortalManager portalManager;

    public LoginTeleporter(PortalManager portalManager) {
        loginTargets = new HashMap<>();
        this.portalManager = portalManager;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        System.out.println("Player " + event.getPlayer().getDisplayName() + " joined.");
        UUID player = event.getPlayer().getUniqueId();
        if(loginTargets.containsKey(player)) {
            System.out.println("Player is in login map thingy");
            Portal portal = portalManager.getPortal(loginTargets.get(player));
            loginTargets.remove(player);
            if(portal != null) {
                System.out.println("Tp player to portal location");
                portal.executeActions(event.getPlayer());
            }
        }
    }

    public void process(String message) {
        int idx = message.indexOf("|");
        if(idx == -1) return;
        
        UUID player = UUID.fromString(message.substring(0, idx));
        if(player == null) return;

        message = message.substring(idx + 1);
        idx = message.indexOf("|");
        if(idx == -1) return;
        
        String portal = message.substring(0, idx);
        if(portalManager.getPortal(portal) == null) return;

        String server = message.substring(idx + 1);
        loginTargets.put(player, portal);
        CVPortal.getInstance().getCVIPC().sendMessage("server|" + player + "|" + server);
    }
}
