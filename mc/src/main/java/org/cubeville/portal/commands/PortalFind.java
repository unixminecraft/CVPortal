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

public class PortalFind extends Command
{
    public PortalFind() {
        super("find");
        addFlag("target");
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        CommandResponse ret = new CommandResponse();
        
        PortalManager portalManager = PortalManager.getInstance();

        for(Portal portal: portalManager.getPortals()) {
            if(flags.contains("target")) {
                if(portal.isPlayerNearTarget(player, 8)) {
                    ret.addMessage(portal.getInfo());
                }
            }
            else {
                if(portal.isPlayerNearPortal(player, 8)) {
                    ret.addMessage(portal.getInfo());
                }
            }
        }

        ret.setBaseMessageIfEmpty("&cNo portals found.");
        return ret;
    }
}
