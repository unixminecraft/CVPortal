package org.cubeville.portal.commands;

import java.util.ArrayList;
import java.util.Collections;
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
        
        boolean nameOnly;
        if(flags.size() == 0) { nameOnly = false; }
        else if(flags.contains("-n")) { nameOnly = true; }
        else { nameOnly = false; }
        
        PortalManager portalManager = PortalManager.getInstance();
        
        if(baseParameters.size() == 0) {
            if(nameOnly) {
                return formatNamesNicely(portalManager.getPortals(), player);
            }
            else {
                CommandResponse ret = new CommandResponse();
                for(Portal portal: portalManager.getPortals()) {
                    if(portal.getWorld().equals(player.getLocation().getWorld().getUID())) {
                        ret.addMessage(portal.getInfo());
                    }
                }
                return ret;
            }
        }
        else {
            CommandResponse ret = new CommandResponse();
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
            if(nameOnly) {
                return formatNamesNicely(matchingPortals, player);
            }
            else {
                for(Portal portal: matchingPortals) {
                    if(portal.getWorld().equals(player.getLocation().getWorld().getUID())) {
                        ret.addMessage(portal.getInfo());
                    }
                }
                return ret;
            }
        }
    }

    private CommandResponse formatNamesNicely(List<Portal> portals, Player player) {
        
        CommandResponse ret = new CommandResponse();
        
        if(portals.size() == 0) {
            ret.addMessage("&cNo portals found.");
            return ret;
        }
        
        List<String> portalNames = new ArrayList<String>();
        for(Portal portal: portals) {
            if(portal.getWorld().equals(player.getLocation().getWorld().getUID())) {
                portalNames.add(portal.getName());
            }
        }
        
        Collections.sort(portalNames);
        
        String finalNames = "";
        if(portalNames.size() == 0) { return new CommandResponse("&cNo portals found."); }
        if(portalNames.size() == 1) {
            finalNames = "&a" + portalNames.get(0);
            ret.addMessage(finalNames);
            return ret;
        }
        finalNames = "&a";
        for(int i = 0; i < portalNames.size() - 1; i++) {
            finalNames += portalNames.get(i);
            finalNames += ", ";
        }
        finalNames += portalNames.get(portalNames.size() - 1);
        
        ret.addMessage(finalNames);
        return ret;
    }
}
