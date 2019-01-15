package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;

public class PortalDelete extends Command
{
    public PortalDelete() {
        super("delete");
        addBaseParameter(new CommandParameterPortal());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {
        
        String name = ((Portal) baseParameters.get(0)).getName();
        PortalManager.getInstance().deletePortal(name);

        return new CommandResponse("&aPortal deleted.");
    }

}
