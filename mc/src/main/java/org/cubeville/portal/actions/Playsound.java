package org.cubeville.portal.actions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Playsound")
public class Playsound implements Action
{
    Sound sound;
    float pitch;
    
    public Playsound(Sound sound, Float pitch) {
        this.sound = sound;
        this.pitch = pitch;
    }

    public Playsound(Map<String, Object> config) {
        this.sound = Sound.valueOf((String) config.get("sound"));
        if(config.get("pitch") != null) {
            this.pitch = ((Double) config.get("pitch")).floatValue();
        }
        else {
            this.pitch = 1.0f;
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("sound", sound.toString());
        ret.put("pitch", pitch);
        return ret;
    }

    public void execute(Player player) {
        player.playSound(player.getLocation(), sound, 1, pitch);
    }

    public String getLongInfo() {
        return " - &bSound: " + sound.toString();
    }

    public String getShortInfo() {
        return "SND";
    }

    public boolean isSingular() {
        return false;
    }

    public Sound getSound() {
        return this.sound;
    }
}
