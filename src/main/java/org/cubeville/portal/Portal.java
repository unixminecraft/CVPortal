package org.cubeville.portal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import org.cubeville.portal.actions.Action;

@SerializableAs("Portal")
public class Portal implements ConfigurationSerializable
{
    private String name;
    private UUID world;
    private Vector minCorner;
    private Vector maxCorner;
    private boolean permanent;
    private boolean active;
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
        return ret;
    }

    public void cyclicTrigger(Collection<Player> players) {
        if(active) trigger(players);
    }
    
    public void trigger(Collection<Player> players) {
        for(Player player: players) trigger(player, false);
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

    private void executeActions(Player player) {
        for(Action action: actions) {
            action.execute(player);
        }        
    }
    
    public boolean isPlayerInPortal(Player player) {
        Location loc = player.getLocation();
        if(!loc.getWorld().getUID().equals(world)) return false;
        Vector vloc = loc.toVector();
        return vloc.isInAABB(minCorner, maxCorner);
    }

    public boolean isPlayerNearPortal(Player player, double radius) {
        Location loc = player.getLocation();
        if(!loc.getWorld().getUID().equals(world)) return false;
        Vector vloc = loc.toVector();
        Vector min = minCorner.clone().subtract(new Vector(radius, radius, radius));
        Vector max = minCorner.clone().add(new Vector(radius, radius, radius));
        return vloc.isInAABB(min, max);
    }

    public String getInfo() {
        String ret = name;
        Vector max = maxCorner.clone().subtract(new Vector(1, 1, 1));
        ret += " (" + minCorner.getX() + "," + minCorner.getY() + "," + minCorner.getZ() + " - " + max.getX() + "," + max.getY() + "," + max.getZ() + ")";

        String a = "";
        for(Action action: actions) {
            if(a.length() > 0) a += ",";
            a += action.getShortInfo();
        }
        if(a.length() > 0) a = " " + a;
        ret += a;
        return ret;
    }

    public void addAction(Action action) {
        actions.add(action);
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
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
