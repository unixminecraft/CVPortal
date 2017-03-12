package org.cubeville.portal;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        UUID player = event.getPlayer().getUniqueId();
        if(loginTargets.containsKey(player)) {
            String target = loginTargets.get(player);
            loginTargets.remove(player);

            if(target.startsWith("portal:")) {
                Portal portal = portalManager.getPortal(target.substring(7));
                if(portal != null) {
                    portal.executeActions(event.getPlayer());
                }
            }
            else if(target.startsWith("player:")) {
                Player targetPlayer = Bukkit.getServer().getPlayer(UUID.fromString(target.substring(7)));
                if(targetPlayer != null) {
                    event.getPlayer().teleport(targetPlayer.getLocation());
                }
            }
            else if(target.startsWith("coord:")) {
                StringTokenizer tk = new StringTokenizer(target.substring(6), ",");
                String world = tk.nextToken();
                int x = Integer.valueOf(tk.nextToken());
                int y = Integer.valueOf(tk.nextToken());
                int z = Integer.valueOf(tk.nextToken());
                event.getPlayer().teleport(new Location(Bukkit.getServer().getWorld(world), x, y, z));
            }
        }
    }

    public void process(String channel, String message) {
        int idx = message.indexOf("|");
        if(idx == -1) return;
        
        UUID player = UUID.fromString(message.substring(0, idx));
        if(player == null) return;

        message = message.substring(idx + 1);
        idx = message.indexOf("|");
        if(idx == -1) return;
        
        String target = message.substring(0, idx);
        String server = message.substring(idx + 1);
        loginTargets.put(player, target);
        CVPortal.getInstance().getCVIPC().sendMessage("server|" + player + "|" + server);
    }
}
