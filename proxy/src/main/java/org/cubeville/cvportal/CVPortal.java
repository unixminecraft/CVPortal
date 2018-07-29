package org.cubeville.cvportal;

import java.io.File;
import java.io.IOException;

import java.util.Map;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import org.cubeville.cvipc.CVIPC;

import org.cubeville.cvportal.commands.BringCommand;
import org.cubeville.cvportal.commands.TpCommand;
import org.cubeville.cvportal.commands.WarpAliasCommand;
import org.cubeville.cvportal.commands.WarpCommand;

import org.cubeville.cvportal.warps.WarpManager;

public class CVPortal extends Plugin
{
    public void onEnable() {
        PluginManager pm = getProxy().getPluginManager();
        CVIPC ipc = (CVIPC) pm.getPlugin("CVIPC");
        pm.registerCommand(this, new TpCommand(ipc));
        pm.registerCommand(this, new BringCommand(ipc));

        File configFile = new File(getDataFolder(), "config.yml");
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            WarpManager warpManager = new WarpManager((Configuration) config.get("warps"), configFile, ipc);
            pm.registerCommand(this, new WarpCommand(warpManager));
            Map<String, String> warpCommands = warpManager.getWarpCommands();
            for(String command: warpCommands.keySet()) {
                pm.registerCommand(this, new WarpAliasCommand(warpManager, command, warpCommands.get(command)));
            }
        }
        catch(IOException e) {
            System.out.println("ERROR: Could not load cvportal warp configuration");
        }
    }
}
