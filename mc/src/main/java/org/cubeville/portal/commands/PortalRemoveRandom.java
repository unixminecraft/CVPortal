package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;
import org.cubeville.portal.actions.Action;
import org.cubeville.portal.actions.Random;

public class PortalRemoveRandom extends Command
{
    public PortalRemoveRandom() {
        super("remove forward");
        addBaseParameter(new CommandParameterPortal());
        addBaseParameter(new CommandParameterString());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(0);
        String targetPortalName = (String) baseParameters.get(1);

        List<Action> randomActions = portal.getActionsByType(Random.class);
        if(randomActions.size() == 0) throw new CommandExecutionException("&cNo forward target portals defined!");
        Random action = (Random) randomActions.get(0);
        if(action.removePortal(targetPortalName)) {
            boolean empty = action.getPortalCount() == 0;
            if(empty) portal.removeActionByType(Random.class, 0);
            PortalManager.getInstance().save();
            return new CommandResponse(empty ? "&aLast portal removed from list of forward targets." : "&aPortal removed.");
        }
        else {
            throw new CommandExecutionException("&cPortal is not in the forward target list!");
        }
    }
}
