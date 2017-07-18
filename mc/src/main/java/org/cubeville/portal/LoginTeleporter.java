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

// TODO: Does more than login by now. Rename?
public class LoginTeleporter implements Listener, IPCInterface
{
    Map<UUID, String> loginTargets;
    PortalManager portalManager;

    public LoginTeleporter(PortalManager portalManager) {
        loginTargets = new HashMap<>();
        this.portalManager = portalManager;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if(loginTargets.containsKey(playerId)) {
            final String target = loginTargets.get(playerId);
            loginTargets.remove(playerId);
            // Need to delay the actual tp by one tick to avoid invisible players, probably
            // interfering with mv? Sucks but works....
            Bukkit.getScheduler().runTaskLater(CVPortal.getInstance(), new Runnable() {
                    public void run() {
                        teleport(player, target);
                    }
                }, 1);
        }
    }

    public void teleport(Player player, String target) {
        System.out.println("Loginteleporter: tp player " + player.getName() + " to " + target);
        if(target.startsWith("portal:")) {
            Portal portal = portalManager.getPortal(target.substring(7));
            if(portal != null) {
                portal.executeActions(player);
            }
        }
        else if(target.startsWith("player:")) {
            Player targetPlayer = Bukkit.getServer().getPlayer(UUID.fromString(target.substring(7)));
            if(targetPlayer != null) {
                player.teleport(targetPlayer.getLocation());
            }
        }
        else if(target.startsWith("coord:")) {
            StringTokenizer tk = new StringTokenizer(target.substring(6), ",");
            String world = tk.nextToken();
            double x = Double.valueOf(tk.nextToken());
            double y = Double.valueOf(tk.nextToken());
            double z = Double.valueOf(tk.nextToken());
            float yaw = 0;
            float pitch = 0;
            if(tk.hasMoreTokens()) yaw = Float.valueOf(tk.nextToken());
            if(tk.hasMoreTokens()) pitch = Float.valueOf(tk.nextToken());
            Location loc = new Location(Bukkit.getServer().getWorld(world), x, y, z, yaw, pitch);
            player.teleport(loc);
        }
    }

    public void setLoginTarget(UUID playerId, String target) {
        loginTargets.put(playerId, target);
    }
    
    public void process(String channel, String message) {
        System.out.println("Portal IPC: " + channel + ": " + message);

        int idx = message.indexOf("|");
        if(idx == -1) return;
        UUID player = UUID.fromString(message.substring(0, idx));
        message = message.substring(idx + 1);

        if(channel.equals("xwportal")) {
            idx = message.indexOf("|");
            if(idx == -1) return;
            String target = message.substring(0, idx);
            String server = message.substring(idx + 1);

            if(Bukkit.getServer().getPlayer(player) != null) {
                System.out.println("Player is online, do direct tp");
                teleport(Bukkit.getServer().getPlayer(player), target);
            }
            else {
                loginTargets.put(player, target);
                CVPortal.getInstance().getCVIPC().sendMessage("server|" + player + "|" + server);
                System.out.println("Set login target for " + player + " to " + target);
            }
        }
        else if(channel.equals("tplocal")) {
            Player p = Bukkit.getServer().getPlayer(player);
            if(p != null) {
                System.out.println("Teleport player " + p.getName() + " locally to target " + message);
                teleport(p, message);
            }
            else {
                System.out.println("Player for teleport could not be found!");
            }
        }
    }
}
