package org.cubeville.portal.actions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

@SerializableAs("Title")
public class Title implements Action
{
    String title;
    String subtitle;
    int fadeIn;
    int stay;
    int fadeOut;

    public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public Title(Map<String, Object> config) {
        this.title = (String) config.get("title");
        this.subtitle = (String) config.get("subtitle");
        this.fadeIn = (int) config.get("fadeIn");
        this.stay = (int) config.get("stay");
        this.fadeOut = (int) config.get("fadeOut");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("title", title);
        ret.put("subtitle", subtitle);
        ret.put("fadeIn", fadeIn);
        ret.put("stay", stay);
        ret.put("fadeOut", fadeOut);
        return ret;
    }

    public void execute(Player player) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    public String getLongInfo() {
        return " - &bTitle: &r" + title + "&r/" + subtitle + " &r(" + fadeIn + "/" + stay + "/" + fadeOut + ")";
    }

    public String getShortInfo() {
        return "TI";
    }

    public boolean isSingular() {
        return true;
    }
}
