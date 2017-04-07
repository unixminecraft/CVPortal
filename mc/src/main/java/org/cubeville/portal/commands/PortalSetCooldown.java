package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterDouble;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.portal.PortalManager;

public class PortalSetCooldown extends Command
{
    public PortalSetCooldown() {
        super("set cooldown");
        addBaseParameter(new CommandParameterString());
        addBaseParameter(new CommandParameterDouble());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        String name = (String) baseParameters.get(0);
        PortalManager portalManager = PortalManager.getInstance();
        if(portalManager.getPortal(name) == null) throw new CommandExecutionException("&cPortal does not exist!");

        double cooldown = (double) baseParameters.get(1);
        portalManager.getPortal(name).setCooldown(new Double(cooldown * 1000).intValue());
        portalManager.save();
        
        return new CommandResponse("&aCooldown set.");   
    }
    
}
