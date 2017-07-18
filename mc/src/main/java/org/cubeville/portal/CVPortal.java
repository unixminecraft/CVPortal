package org.cubeville.portal;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.cubeville.commons.commands.CommandParser;

import org.cubeville.cvipc.CVIPC;

import org.cubeville.portal.actions.*;
import org.cubeville.portal.commands.*;

public class CVPortal extends JavaPlugin {

    private PortalManager portalManager;
    private CommandParser commandParser;
    private CommandParser tpposCommandParser;
    private LoginTeleporter loginTeleporter;
    
    private CVIPC cvipc;

    private static CVPortal instance;
    public static CVPortal getInstance() {
        return instance;
    }

    public CVIPC getCVIPC() {
        return cvipc;
    }
    
    public void onEnable() {
        instance = this;
        
        ConfigurationSerialization.registerClass(Portal.class);
        ConfigurationSerialization.registerClass(Teleport.class);
        ConfigurationSerialization.registerClass(CrossServerTeleport.class);
        ConfigurationSerialization.registerClass(Message.class);
        ConfigurationSerialization.registerClass(ClearInventory.class);
        ConfigurationSerialization.registerClass(Heal.class);
        ConfigurationSerialization.registerClass(RemoveEffects.class);
        ConfigurationSerialization.registerClass(Cmd.class);

        portalManager = new PortalManager(this);
        portalManager.start();

        PluginManager pm = getServer().getPluginManager();
        cvipc = (CVIPC) pm.getPlugin("CVIPC");
        loginTeleporter = new LoginTeleporter(portalManager);
        pm.registerEvents(loginTeleporter, this);
        cvipc.registerInterface("xwportal", loginTeleporter);
        cvipc.registerInterface("tplocal", loginTeleporter);
        
        commandParser = new CommandParser();
        commandParser.addCommand(new PortalCreate());
        commandParser.addCommand(new PortalSetCrossServerTeleport());
        commandParser.addCommand(new PortalDelete());
        commandParser.addCommand(new PortalFind());
        commandParser.addCommand(new PortalList());
        commandParser.addCommand(new PortalSelect());
        commandParser.addCommand(new PortalSetCmd());
        commandParser.addCommand(new PortalSetCooldown());
        commandParser.addCommand(new PortalSetMessage());
        commandParser.addCommand(new PortalSetTeleport());
        commandParser.addCommand(new PortalSet());
        commandParser.addCommand(new PortalLoginTarget(loginTeleporter));
        
        tpposCommandParser = new CommandParser();
        tpposCommandParser.addCommand(new Tppos());
            }

    public void onDisable() {
        portalManager.stop();
        cvipc.deregisterInterface("xwportal");
        instance = null;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("cvportal")) {
            return commandParser.execute(sender, args);
        }
        else if(command.getName().equals("tppos")) {
            return tpposCommandParser.execute(sender, args);
        }
        return false;
    }
        
}
