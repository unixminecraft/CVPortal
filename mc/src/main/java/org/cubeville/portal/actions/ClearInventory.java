package org.cubeville.portal.actions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("ClearInventory")
public class ClearInventory implements Action
{
    public ClearInventory() {}
    
    public ClearInventory(Map<String, Object> config) {
    }
    
    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        return ret;
    }
    
    public void execute(Player player) {
        player.getInventory().clear();
    }

    public String getLongInfo() {
        return " - &bClear Inventory";
    }
    
    public String getShortInfo() {
        return "CI";
    }

    public boolean isSingular() {
        return true;
    }
}
