package org.cubeville.portal.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterEnumeratedString;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;

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
        addBaseParameter(new CommandParameterString());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        String name = (String) baseParameters.get(1);
        PortalManager portalManager = PortalManager.getInstance();
        if(portalManager.getPortal(name) == null) throw new CommandExecutionException("&cPortal does not exist!");

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
        portalManager.getPortal(name).addAction(action);
        portalManager.save();

        return new CommandResponse("&aAction " + actionPar + " set.");
    }
}
