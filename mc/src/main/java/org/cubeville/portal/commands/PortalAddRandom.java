package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.commons.commands.CommandParameterInteger;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;
import org.cubeville.portal.actions.Action;
import org.cubeville.portal.actions.Random;

public class PortalAddRandom extends Command
{
    public PortalAddRandom() {
        super("add forward");
        addBaseParameter(new CommandParameterPortal());
        addBaseParameter(new CommandParameterPortal());
        addOptionalBaseParameter(new CommandParameterInteger());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(0);
        Portal targetPortal = (Portal) baseParameters.get(1);
        
        int weight = 0;
        if(baseParameters.size() == 3) weight = (Integer) baseParameters.get(2);

        Random action;
        List<Action> randomActions = portal.getActionsByType(Random.class);
        if(randomActions.size() == 0) {
            action = new Random();
            portal.addAction(action);
        }
        else {
            action = (Random) randomActions.get(0);
        }
        action.addPortal(targetPortal.getName(), weight);
        PortalManager.getInstance().save();
        
        return new CommandResponse("&aPortal added.");
    }
    
}
