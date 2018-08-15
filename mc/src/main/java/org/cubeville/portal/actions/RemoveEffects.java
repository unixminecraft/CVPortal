package org.cubeville.portal.actions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.potion.PotionEffect;

@SerializableAs("RemoveEffects")
public class RemoveEffects implements Action
{
    public RemoveEffects() {}
    
    public RemoveEffects(Map<String, Object> config) {
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        return ret;
    }

    public void execute(Player player) {
        for(PotionEffect effect: player.getActivePotionEffects()) {
            if(player.hasPotionEffect(effect.getType())) {
                player.removePotionEffect(effect.getType());
            }
        }
    }

    public String getLongInfo() {
        return " - &bRemove Effects";
    }
    
    public String getShortInfo() {
        return "RE";
    }

    public boolean isSingular() {
        return true;
    }
}
