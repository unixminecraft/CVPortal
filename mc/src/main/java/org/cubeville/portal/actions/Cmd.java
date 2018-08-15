package org.cubeville.portal.actions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

@SerializableAs("Cmd")
public class Cmd implements Action
{
    String cmd;

    public Cmd(String cmd) {
        this.cmd = cmd;
    }

    public Cmd(Map<String, Object> config) {
        this.cmd = (String) config.get("cmd");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("cmd", cmd);
        return ret;
    }

    public void execute(Player player) {
        Bukkit.dispatchCommand(player, cmd);
    }
    
    public String getLongInfo() {
        return " - &bCommand: " + cmd;
    }

    public String getShortInfo() {
        return "CMD";
    }

    public boolean isSingular() {
        return false;
    }
}
