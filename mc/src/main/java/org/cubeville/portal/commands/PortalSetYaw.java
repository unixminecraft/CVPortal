package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterInteger;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;

public class PortalSetYaw extends Command
{
    public PortalSetYaw() {
        super("set yaw");
        addBaseParameter(new CommandParameterPortal());
        addBaseParameter(new CommandParameterInteger());
        addBaseParameter(new CommandParameterInteger());        
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(0);

        int minYaw = (int) baseParameters.get(1);
        int maxYaw = (int) baseParameters.get(2);
        portal.setYaw(minYaw, maxYaw);
        PortalManager.getInstance().save();

        return new CommandResponse("&aYaw set.");
    }
}
