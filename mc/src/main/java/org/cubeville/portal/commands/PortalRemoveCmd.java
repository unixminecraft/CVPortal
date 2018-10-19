package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterInteger;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.portal.PortalManager;

public class PortalRemoveCmd extends Command {

    public PortalRemoveCmd() {
        super("remove command");
        addBaseParameter(new CommandParameterString());
        addBaseParameter(new CommandParameterInteger());
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
            throws CommandExecutionException {
        String portalName = (String) baseParameters.get(0);
        PortalManager portalManager = PortalManager.getInstance();
        if(portalManager.getPortal(portalName) == null ) {
            throw new CommandExecutionException("&cPortal does not exist.");
        }
        int commandIndex = (int) baseParameters.get(1);
        if(commandIndex < 1 || commandIndex > portalManager.getPortal(portalName).countCmds()) {
            throw new CommandExecutionException("&cPlease use a valid index between 1 and " + portalManager.getPortal(portalName).countCmds() + "!");
        }
        commandIndex--;
        portalManager.getPortal(portalName).removeCmd(commandIndex);
        portalManager.save();
        return new CommandResponse("&aCommand removed.");
    }

}
