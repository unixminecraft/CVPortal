package org.cubeville.portal.actions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

@SerializableAs("Velocity")
public class Velocity implements Action
{
    Vector velocity;

    public Velocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Velocity(Map<String, Object> config) {
        this.velocity = (Vector) config.get("velocity");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("velocity", velocity);
        return ret;
    }

    public void execute(Player player) {
        player.setVelocity(velocity);
    }

    public String getShortInfo() {
        return "VEL";
    }

    public boolean isSingular() {
        return true;
    }

    public Vector getVelocity() {
        return this.velocity;
    }
}
