package org.cubeville.portal;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import org.cubeville.commons.commands.CommandParser;

import org.cubeville.portal.actions.*;
import org.cubeville.portal.commands.*;

public class CVPortal extends JavaPlugin {

    private PortalManager portalManager;
    private CommandParser commandParser;
    
    public void onEnable() {
        ConfigurationSerialization.registerClass(Portal.class);
        ConfigurationSerialization.registerClass(Teleport.class);
        ConfigurationSerialization.registerClass(Message.class);
        ConfigurationSerialization.registerClass(ClearInventory.class);
        ConfigurationSerialization.registerClass(Heal.class);
        ConfigurationSerialization.registerClass(RemoveEffects.class);

        portalManager = new PortalManager(this);
        portalManager.start();

        commandParser = new CommandParser();
        commandParser.addCommand(new PortalCreate());
        commandParser.addCommand(new PortalDelete());
        commandParser.addCommand(new PortalFind());
        commandParser.addCommand(new PortalList());
        commandParser.addCommand(new PortalSelect());
        commandParser.addCommand(new PortalSet());
        commandParser.addCommand(new PortalSetCooldown());
        commandParser.addCommand(new PortalSetMessage());
        commandParser.addCommand(new PortalSetTeleport());
    }

    public void onDisable() {
        portalManager.stop();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("cvportal")) {
            return commandParser.execute(sender, args);
        }
        return false;
    }
        
}
