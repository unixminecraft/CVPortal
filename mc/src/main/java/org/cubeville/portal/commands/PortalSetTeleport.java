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
import org.cubeville.portal.actions.Teleport;

public class PortalSetTeleport extends Command
{
    public PortalSetTeleport() {
        super("set teleport");
        addBaseParameter(new CommandParameterPortal());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(0);

        portal.addAction(new Teleport(player.getLocation()));
        PortalManager.getInstance().save();
        
        return new CommandResponse("&aTeleport location set.");
    }
}
