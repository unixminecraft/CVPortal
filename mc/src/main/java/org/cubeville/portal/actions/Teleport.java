package org.cubeville.portal.actions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Teleport")
public class Teleport implements Action
{
    Location location;
    
    public Teleport(Location location) {
        this.location = location;
    }

    public Teleport(Map<String, Object> config) {
        this.location = (Location) config.get("location");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("location", location);
        return ret;
    }

    public void execute(Player player) {
        player.teleport(location);
    }
    
    public String getLongInfo() {
        return " - &bTeleport: " + location;
    }

    public String getShortInfo() {
        return "TP";
    }

    public boolean isSingular() {
        return true;
    }

    public Location getLocation() {
        return location;
    }
}
