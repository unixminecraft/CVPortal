package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.commons.utils.BlockUtils;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;


public class PortalRedefine extends Command
{
    public PortalRedefine() {
        super("redefine");
        addBaseParameter(new CommandParameterString());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        String name = (String) baseParameters.get(0);

        PortalManager portalManager = PortalManager.getInstance();
        Portal portal = portalManager.getPortal(name);
        if(portal == null) throw new CommandExecutionException("&cPortal not found!");

        Vector min = BlockUtils.getWESelectionMin(player).toVector();
        Vector max = BlockUtils.getWESelectionMax(player).toVector();
        max = max.add(new Vector(1.0, 1.0, 1.0));

        portal.redefine(player.getLocation().getWorld(), min, max);
        portalManager.save();

        return new CommandResponse("&aPortal redefined.");
    }

}
