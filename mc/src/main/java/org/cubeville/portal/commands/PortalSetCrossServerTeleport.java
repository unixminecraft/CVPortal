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
import org.cubeville.portal.actions.CrossServerTeleport;

public class PortalSetCrossServerTeleport extends Command
{
    public PortalSetCrossServerTeleport() {
        super("set crossserver teleport");
        addBaseParameter(new CommandParameterPortal()); // portal
        addBaseParameter(new CommandParameterString()); // server
        addBaseParameter(new CommandParameterString()); // target portal
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        String server = (String) baseParameters.get(1);
        String targetPortal = (String) baseParameters.get(2);

        Portal portal = (Portal) baseParameters.get(0);

        portal.addAction(new CrossServerTeleport(server, targetPortal));
        PortalManager.getInstance().save();

        return new CommandResponse("&aCross server teleport portal set. Existence is not verified!");
    }
}
