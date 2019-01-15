package org.cubeville.portal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
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
    private boolean deathTriggered;
    private int cooldown;
    private String permission;
    private int minYaw;
    private int maxYaw;
    
    private Map<UUID, Long> cooldownTimer = new HashMap<>();

    private List<Action> actions;
    
    public Portal(String name, World world, Vector minCorner, Vector maxCorner) {
        this.name = name;
        this.minCorner = minCorner;
        this.maxCorner = maxCorner;
        this.world = world.getUID();
        this.cooldown = 0;
        this.permanent = true;
        this.deathTriggered = false;
        this.permission = null;
        this.minYaw = 0;
        this.maxYaw = 0;
        actions = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public Portal(Map<String, Object> config) {
        minCorner = (Vector) config.get("minCorner");
        maxCorner = (Vector) config.get("maxCorner");
        world = UUID.fromString((String) config.get("world"));
        name = (String) config.get("name");
        permanent = (boolean) config.get("permanent");
        actions = (List<Action>) config.get("actions");
        cooldown = (int) config.get("cooldown");
        permission = null;
        if(config.get("permission") != null) {
            permission = (String) config.get("permission");
            if(permission.equals("")) permission = null;
        }
        if(config.get("minYaw") != null && config.get("maxYaw") != null) {
            minYaw = (int) config.get("minYaw");
            maxYaw = (int) config.get("maxYaw");
        }
        else {
            minYaw = 0;
            maxYaw = 0;
        }
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
        ret.put("permission", permission == null ? "" : permission);
        ret.put("minYaw", minYaw);
        ret.put("maxYaw", maxYaw);
        return ret;
    }

    public void cyclicTrigger(Collection<Player> players) {
        if(permanent) trigger(players);
    }
    
    public void trigger(Collection<Player> players) {
        for(Player player: players) trigger(player, false);
    }

    public boolean trigger(Player player) {
        return trigger(player, false);
    }

    private boolean playerHasPermission(Player player) {
        if(permission == null || permission.equals("")) return true;
        return player.hasPermission("cvportal.portal." + permission);
    }
    
    public void triggerRandom(Collection<Player> players, int count) {
        List<Player> playersInPortal = new ArrayList<>();
        for(Player player: players) {
            if(isPlayerInPortal(player) && playerHasPermission(player)) {
                playersInPortal.add(player);
            }
        }
        
        Random rnd = new Random();
        while(playersInPortal.size() > 0 && count > 0) {
            int i = rnd.nextInt(playersInPortal.size());
            executeActions(playersInPortal.remove(i));
            count--;
        }
    }
    
    public boolean trigger(Player player, boolean overrideCooldown) {
        if(isPlayerInPortal(player) && playerHasPermission(player)) {
            if(cooldown == 0 || overrideCooldown) {
                executeActions(player);
                return true;
            }
            else {
                long now = System.currentTimeMillis();
                UUID uuid = player.getUniqueId();
                if(cooldownTimer.containsKey(uuid)) {
                    if(now - cooldownTimer.get(uuid) < cooldown) return false;
                }
                cooldownTimer.put(uuid, now);
                executeActions(player);
                return true;
            }
        }
        return false;
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

    public boolean isPlayerNearTarget(Player player, double radius) {
        Location tloc = getTeleportLocation();
        if(tloc == null) return false;
        Location ploc = player.getLocation();
        if(!ploc.getWorld().getUID().equals(tloc.getWorld().getUID())) return false;
        return (ploc.distance(tloc) < radius);
    }
    
    public boolean isPlayerInPortal(Player player) {
        if(minCorner == null) return false;
        Location loc = player.getLocation();
        if(!loc.getWorld().getUID().equals(world)) return false;
        Vector vloc = loc.toVector();
        if(!vloc.isInAABB(minCorner, maxCorner)) return false;
        if(minYaw != maxYaw) {
            int pyaw = (int) loc.getYaw();
            while(pyaw < -180) pyaw += 360;
            while(pyaw > 180) pyaw -= 360;
            if(minYaw < maxYaw) {
                if(pyaw < minYaw || pyaw > maxYaw) return false;
            }
            else {
                if(pyaw < minYaw && pyaw > maxYaw) return false;
            }
        }
        return true;
    }

    public boolean isPlayerNearPortal(Player player, double radius) {
        if(minCorner == null) return false;
        Location loc = player.getLocation();
        if(!loc.getWorld().getUID().equals(world)) return false;
        Vector vloc = loc.toVector();
        Vector min = minCorner.clone().subtract(new Vector(radius, radius, radius));
        Vector max = maxCorner.clone().add(new Vector(radius, radius, radius));
        return vloc.isInAABB(min, max);
    }

    public String getInfo() {
        String ret = name;
        if(minCorner == null) {
            ret += " (regionless)";
        }
        else {
            Vector max = maxCorner.clone().subtract(new Vector(1, 1, 1));
            ret += " (" + minCorner.getX() + "," + minCorner.getY() + "," + minCorner.getZ() + " - " + max.getX() + "," + max.getY() + "," + max.getZ();
            if(minYaw != maxYaw) {
                ret += "; " + minYaw + "-" + maxYaw;
            }
            ret += ")";
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
            String sloc = "&6Region: &a" + minCorner.getX() + "," + minCorner.getY() + "," + minCorner.getZ() + " - " + max.getX() + "," + max.getY() + "," + max.getZ();
            if(minYaw != maxYaw) {
                sloc += "; " + minYaw + "-" + maxYaw;
            }
            sloc += " in " + Bukkit.getWorld(world).getName();
            ret.add(sloc);
        }
        String permissionString = "&cNone";
        if(permission != null && (!permission.equals(""))) permissionString = "&a" + permission;
        ret.add("&bPermission: " + permissionString);
        String parameter = "";
        parameter += "&bPermanent: ";
        if(permanent) { parameter += "&aEnabled"; }
        else { parameter += "&cDisabled"; }
        ret.add(parameter);
        parameter = "";
        parameter += "&bDeath Triggered: ";
        if(deathTriggered) { parameter += "&aEnabled"; }
        else { parameter += "&cDisabled"; }
        ret.add(parameter);
        parameter = "";
        parameter += "&bCooldown Time (seconds): ";
        if((cooldown / 1000) <= 0) { parameter += "&cNo cooldown"; }
        else { parameter += ("&a" + (cooldown / 1000)); }
        ret.add(parameter);
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

    public List<Action> getActionsByType(Class<? extends Action> Type) {
        List<Action> ret = new ArrayList<>();
        for(Action action: actions) {
            if(action.getClass().getName().equals(Type.getName())) {
                ret.add(action);
            }
        }
        return ret;
    }

    public int countActionsByType(Class<? extends Action> Type) {
        int ret = 0;
        for(Action action: actions) {
            if(action.getClass().getName().equals(Type.getName())) {
                ret++;
            }
        }
        return ret;
    }

    public boolean removeActionByType(Class<? extends Action> Type, int Index) {
        int aIndex = 0;
        for(int i = 0; i < actions.size(); i++) {
            if(actions.get(i).getClass().getName().equals(Type.getName())) {
                if(aIndex == Index) {
                    actions.remove(i);
                    return true;
                }
                aIndex++;
            }
        }
        return false;
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
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
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

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setYaw(int minYaw, int maxYaw) {
        this.minYaw = minYaw;
        this.maxYaw = maxYaw;
    }
}
