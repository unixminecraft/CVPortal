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

public class Ptp extends Command
{
    public Ptp() {
        super("");
        addBaseParameter(new CommandParameterString());
        addFlag("addworldname");
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        String name = (String) baseParameters.get(0);
        String portalName = name;
        if(flags.contains("addworldname")) portalName += "_" + player.getLocation().getWorld().getName();

        PortalManager portalManager = PortalManager.getInstance();
        Portal portal = portalManager.getPortal(portalName);
        if(portal == null) throw new CommandExecutionException("&cTarget does not exist!");

        if(!player.hasPermission("cvportal.ptp." + name.toLowerCase())) throw new CommandExecutionException("&cNo permission.");
        portal.executeActions(player);
        return new CommandResponse("");
    }
}
