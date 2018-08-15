package org.cubeville.portal.actions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

@SerializableAs("Message")
public class Message implements Action
{
    String message;

    public Message(String message) {
        this.message = message;
    }

    public Message(Map<String, Object> config) {
        this.message = (String) config.get("message");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("message", message);
        return ret;
    }

    public void execute(Player player) {
        player.sendMessage(message);
    }

    public String getLongInfo() {
        return " - &bMessage: " + message;
    }
    
    public String getShortInfo() {
        return "MSG";
    }

    public boolean isSingular() {
        return true;
    }    
}
