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
    private CommandParser ptpCommandParser;
    private CommandParser tpposCommandParser;
    private CommandParser setwarpCommandParser;
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
        ConfigurationSerialization.registerClass(Random.class);
        ConfigurationSerialization.registerClass(RemoveEffects.class);
        ConfigurationSerialization.registerClass(Cmd.class);
        ConfigurationSerialization.registerClass(SuCmd.class);
        ConfigurationSerialization.registerClass(Velocity.class);
        
        portalManager = new PortalManager(this);
        portalManager.start();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(portalManager, this);
        
        cvipc = (CVIPC) pm.getPlugin("CVIPC");
        loginTeleporter = new LoginTeleporter(portalManager);
        pm.registerEvents(loginTeleporter, this);
        cvipc.registerInterface("xwportal", loginTeleporter);
        cvipc.registerInterface("tplocal", loginTeleporter);
        
        commandParser = new CommandParser();
        commandParser.addCommand(new PortalAddRandom());
        commandParser.addCommand(new PortalCreate());
        commandParser.addCommand(new PortalDelete());
        commandParser.addCommand(new PortalFind());
        commandParser.addCommand(new PortalInfo());
        commandParser.addCommand(new PortalList());
        commandParser.addCommand(new PortalLoginTarget(loginTeleporter));
        commandParser.addCommand(new PortalRedefine());
        commandParser.addCommand(new PortalRemoveCmd());
        commandParser.addCommand(new PortalRemoveRandom());
        commandParser.addCommand(new PortalSelect());
        commandParser.addCommand(new PortalSendMessage());
        commandParser.addCommand(new PortalSet());
        commandParser.addCommand(new PortalSetCmd());
        commandParser.addCommand(new PortalSetCooldown());
        commandParser.addCommand(new PortalSetCrossServerTeleport());
        commandParser.addCommand(new PortalSetDeathTriggered());
        commandParser.addCommand(new PortalSetMessage());
        commandParser.addCommand(new PortalSetPermanent());
        commandParser.addCommand(new PortalSetSuCmd());
        commandParser.addCommand(new PortalSetTeleport());
        commandParser.addCommand(new PortalSetVelocity());
        commandParser.addCommand(new PortalTrigger());

        tpposCommandParser = new CommandParser();
        tpposCommandParser.addCommand(new Tppos());

        ptpCommandParser = new CommandParser();
        ptpCommandParser.addCommand(new Ptp());

        setwarpCommandParser = new CommandParser();
        setwarpCommandParser.addCommand(new Setwarp());
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
        else if(command.getName().equals("ptp")) {
            return ptpCommandParser.execute(sender, args);
        }
        else if(command.getName().equals("setwarp")) {
            return setwarpCommandParser.execute(sender, args);
        }
        return false;
    }

}
