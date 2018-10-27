package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.commons.commands.CommandParameterInteger;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;
import org.cubeville.portal.actions.Action;
import org.cubeville.portal.actions.Random;

public class PortalAddRandom extends Command
{
    public PortalAddRandom() {
        super("add random");
        addBaseParameter(new CommandParameterString());
        addBaseParameter(new CommandParameterString());
        addOptionalBaseParameter(new CommandParameterInteger());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        String portalName = (String) baseParameters.get(0);
        PortalManager portalManager = PortalManager.getInstance();
        Portal portal = portalManager.getPortal(portalName);
        if(portal == null) throw new CommandExecutionException("&cPortal does not exist!");
        
        String targetPortalName = (String) baseParameters.get(1);
        int weight = 10;
        if(baseParameters.size() == 3) weight = (Integer) baseParameters.get(2);

        Random action;
        List<Action> randomActions = portal.getActionsByType(Random.class);
        if(randomActions.size() == 0) {
            action = new Random();
            portal.addAction(action);
        }
        else {
            action = (Random)randomActions.get(0);
        }
        action.addPortal(targetPortalName, weight);
        portalManager.save();
        
        return new CommandResponse("&aPortal added.");
    }
    
}
