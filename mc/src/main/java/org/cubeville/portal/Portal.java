package org.cubeville.portal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import org.cubeville.portal.actions.Action;
import org.cubeville.portal.actions.Teleport;

@SerializableAs("Portal")
public class Portal implements ConfigurationSerializable
{
    private String name;
    private UUID world;
    private Vector minCorner;
    private Vector maxCorner;
    private boolean permanent;
    private boolean active;
    private boolean deathTriggered;
    private int cooldown;
    
    private Map<UUID, Long> cooldownTimer = new HashMap<>();

    private List<Action> actions;
    
    public Portal(String name, World world, Vector minCorner, Vector maxCorner) {
        this.name = name;
        this.minCorner = minCorner;
        this.maxCorner = maxCorner;
        this.world = world.getUID();
        this.cooldown = 0;
        this.permanent = true;
        this.active = true;
        this.deathTriggered = false;
        actions = new ArrayList<>();
    }

    public Portal(Map<String, Object> config) {
        minCorner = (Vector) config.get("minCorner");
        maxCorner = (Vector) config.get("maxCorner");
        world = UUID.fromString((String) config.get("world"));
        name = (String) config.get("name");
        permanent = (boolean) config.get("permanent");
        actions = (List<Action>) config.get("actions");
        cooldown = (int) config.get("cooldown");
        active = permanent;
        if(config.get("deathTriggered") == null) deathTriggered = false;
        else deathTriggered = (boolean) config.get("deathTriggered");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("minCorner", minCorner);
        ret.put("maxCorner", maxCorner);
        ret.put("world", world.toString());
        ret.put("name", name);
        ret.put("permanent", permanent);
        ret.put("actions", actions);
        ret.put("cooldown", cooldown);
        ret.put("deathTriggered", deathTriggered);
        return ret;
    }

    public void cyclicTrigger(Collection<Player> players) {
        if(active) trigger(players);
    }
    
    public void trigger(Collection<Player> players) {
        for(Player player: players) trigger(player, false);
    }

    public void trigger(Player player) {
        trigger(player, false);
    }

    public void triggerRandom(Collection<Player> players) {
        List<Player> playersInPortal = new ArrayList<>();
        for(Player player: players) {
            if(isPlayerInPortal(player)) {
                playersInPortal.add(player);
            }
        }
        if(playersInPortal.size() > 0) {
            Random rnd = new Random();
            int i = rnd.nextInt(playersInPortal.size());
            executeActions(playersInPortal.get(i));
        }
    }
    
    public void trigger(Player player, boolean overrideCooldown) {
        if(isPlayerInPortal(player)) {
            if(cooldown == 0 || overrideCooldown) {
                executeActions(player);
            }
            else {
                long now = System.currentTimeMillis();
                UUID uuid = player.getUniqueId();
                if(cooldownTimer.containsKey(uuid)) {
                    if(now - cooldownTimer.get(uuid) < cooldown) return;
                }
                cooldownTimer.put(uuid, now);
                executeActions(player);
            }
        }
    }

    public void sendMessage(Collection<Player> players, String message) {
        for(Player player: players) {
            if(isPlayerInPortal(player)) {
                player.sendMessage(message);
            }
        }
    }
    
    public void executeActions(Player player) {
        String actionList = "";
        for(Action action: actions) {
            if(actionList.length() > 0) actionList += ",";
            actionList += action.getShortInfo();
            action.execute(player);
        }
        System.out.println("Portal " + name + " triggered for player " + player.getName() + " (" + actionList + ")");
    }

    public Location getTeleportLocation() {
        for(Action action: actions) {
            if(action instanceof Teleport) {
                return ((Teleport) action).getLocation();
            }
        }
        return null;
    }
    
    public boolean isPlayerInPortal(Player player) {
        if(minCorner == null) return false;
        Location loc = player.getLocation();
        if(!loc.getWorld().getUID().equals(world)) return false;
        Vector vloc = loc.toVector();
        return vloc.isInAABB(minCorner, maxCorner);
    }

    public boolean isPlayerNearPortal(Player player, double radius) {
        if(minCorner == null) return false;
        Location loc = player.getLocation();
        if(!loc.getWorld().getUID().equals(world)) return false;
        Vector vloc = loc.toVector();
        Vector min = minCorner.clone().subtract(new Vector(radius, radius, radius));
        Vector max = minCorner.clone().add(new Vector(radius, radius, radius));
        return vloc.isInAABB(min, max);
    }

    public String getInfo() {
        String ret = name;
        if(minCorner == null) {
            ret += " (regionless)";
        }
        else {
            Vector max = maxCorner.clone().subtract(new Vector(1, 1, 1));
            ret += " (" + minCorner.getX() + "," + minCorner.getY() + "," + minCorner.getZ() + " - " + max.getX() + "," + max.getY() + "," + max.getZ() + ")";
        }

        String a = "";
        for(Action action: actions) {
            if(a.length() > 0) a += ",";
            a += action.getShortInfo();
        }
        if(a.length() > 0) a = " " + a;
        ret += a;
        return ret;
    }
    
    public List<String> getLongInfo() {
        List<String> ret = new ArrayList<String>();
        ret.add("&6" + name + "&r:");
        if(minCorner == null) {
            ret.add("&6Region: &cregionless");
        }
        else {
            Vector max = maxCorner.clone().subtract(new Vector(1, 1, 1));
            ret.add("&6Region: &a(" + minCorner.getX() + "," + minCorner.getY() + "," + minCorner.getZ() + ") to (" + max.getX() + "," + max.getY() + "," + max.getZ() + ")");
        }
        ret.add("&6Actions:");
        for(Action action: actions) {
            ret.add(action.getLongInfo());
        }
        return ret;
    }

    public void addAction(Action action) {
        if(action.isSingular()) {
            String t = action.getClass().getName();
            for(int i = actions.size() - 1; i >= 0; i--) {
                if(actions.get(i).getClass().getName().equals(t)) {
                    actions.remove(i);
                }
            }
        }
        actions.add(action);
    }

    public void redefine(World world, Vector minCorner, Vector maxCorner) {
        this.world = world.getUID();
        this.minCorner = minCorner;
        this.maxCorner = maxCorner;
    }

    public String getName() {
        return name;
    }

    public Vector getMinCorner() {
        return minCorner;
    }

    public Vector getMaxCorner() {
        return maxCorner;
    }

    public UUID getWorld() {
        return world;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
        active = permanent;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isDeathTriggered() {
        return deathTriggered;
    }

    public void setDeathTriggered(boolean deathTriggered) {
        this.deathTriggered = deathTriggered;
    }
}
