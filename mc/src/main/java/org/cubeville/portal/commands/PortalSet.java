package org.cubeville.portal.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterEnumeratedString;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;
import org.cubeville.portal.actions.Action;
import org.cubeville.portal.actions.ClearInventory;
import org.cubeville.portal.actions.Heal;
import org.cubeville.portal.actions.RemoveEffects;

public class PortalSet extends Command
{
    public PortalSet() {
        super("set");
        Set<String> pars = new HashSet<>();
        pars.add("clearinventory");
        pars.add("removeeffects");
        pars.add("heal");
        addBaseParameter(new CommandParameterEnumeratedString(pars));
        addBaseParameter(new CommandParameterPortal());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(1);

        Action action;
        String actionPar = (String) baseParameters.get(0);
        if(actionPar.equals("clearinventory")) {
            action = new ClearInventory();
        }
        else if(actionPar.equals("removeeffects")) {
            action = new RemoveEffects();
        }
        else if(actionPar.equals("heal")) {
            action = new Heal();
        }
        else {
            throw new CommandExecutionException("Unknown action " + actionPar);
        }

        portal.addAction(action);
        PortalManager.getInstance().save();

        return new CommandResponse("&aAction " + actionPar + " set.");
    }
}
