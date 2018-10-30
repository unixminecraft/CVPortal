package org.cubeville.portal.actions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Heal")
public class Heal implements Action
{
    public Heal() {}
    
    public Heal(Map<String, Object> config) {
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        return ret;
    }

    @SuppressWarnings("deprecation")
    public void execute(Player player) {
        player.setHealth(player.getMaxHealth());
    }

    public String getLongInfo() {
        return " - &bHealing";
    }
    
    public String getShortInfo() {
        return "H";
    }

    public boolean isSingular() {
        return true;
    }    
}
