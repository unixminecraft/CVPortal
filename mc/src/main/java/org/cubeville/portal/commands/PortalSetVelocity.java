package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterVector;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;
import org.cubeville.portal.actions.Velocity;

public class PortalSetVelocity extends Command
{
    public PortalSetVelocity() {
        super("set velocity");
        setPermission("cvportal.setvelocity");
        addBaseParameter(new CommandParameterPortal());
        addBaseParameter(new CommandParameterVector());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(0);

        Vector velocity = (Vector) baseParameters.get(1);
        portal.addAction(new Velocity(velocity));
        PortalManager.getInstance().save();

        return new CommandResponse("&aVelocity set.");
    }

}
