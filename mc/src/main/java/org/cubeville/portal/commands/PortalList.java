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

public class PortalList extends Command
{
    public PortalList() {
        super("list");
        addFlag("-n");
        addOptionalBaseParameter(new CommandParameterString());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        /*
        CommandResponse ret = new CommandResponse();
        
        PortalManager portalManager = PortalManager.getInstance();
        for(Portal portal: portalManager.getPortals()) {
            if(portal.getWorld().equals(player.getLocation().getWorld().getUID())) {
                ret.addMessage(portal.getInfo());
            }
        }
        */
        
        boolean nameOnly;
        if(flags.size() == 0) { nameOnly = false; }
        else if(flags.contains("-n")) { nameOnly = true; }
        else { nameOnly = false; }
        
        PortalManager portalManager = PortalManager.getInstance();
        CommandResponse ret = new CommandResponse();
        
        if(baseParameters.size() == 0) {
            for(Portal portal: portalManager.getPortals()) {
                if(portal.getWorld().equals(player.getLocation().getWorld().getUID())) {
                    if(nameOnly) { ret.addMessage(portal.getName()); }
                    else { ret.addMessage(portal.getInfo()); }
                }
            }
            return ret;
        }
        else {
            String search = (String) baseParameters.get(0);
            if(search.length() < 4) {
                ret.addMessage("&cPlease have a search parameter of at least 4 characters.");
                return ret;
            }
            List<Portal> matchingPortals = portalManager.getMatchingPortals(search);
            if(matchingPortals.size() == 0) {
                ret.addMessage("&cNo portals found.");
                return ret;
            }
            for(Portal portal: matchingPortals) {
                if(portal.getWorld().equals(player.getLocation().getWorld().getUID())) {
                    if(nameOnly) { ret.addMessage(portal.getName()); }
                    else { ret.addMessage(portal.getInfo()); }
                }
            }
            return ret;
        }
    }

}
