package org.cubeville.portal.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.PortalManager;

public class PortalTrigger extends BaseCommand
{
    public PortalTrigger() {
        super("trigger");
        addBaseParameter(new CommandParameterString());
        addFlag("random");
        addParameter("player", true, new CommandParameterString());
        addFlag("force");
    }

    @SuppressWarnings("unchecked")
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {
        String name = (String) baseParameters.get(0);
        PortalManager portalManager = PortalManager.getInstance();
        if(portalManager.getPortal(name) == null) throw new CommandExecutionException("&cPortal does not exist!");

        if(flags.contains("random") && parameters.get("player") != null) throw new CommandExecutionException("&cplayer and random parameters are exclusive!");
        Collection<Player> players = (Collection<Player>) Bukkit.getServer().getOnlinePlayers();

        if(flags.contains("force") && parameters.get("player") == null) throw new CommandExecutionException("&cforce can only be used with the player paramtere!");
        
        if(flags.contains("random")) {
            portalManager.getPortal(name).triggerRandom(players);
        }
        else if(parameters.containsKey("player")) {
            String playerName = (String) parameters.get("player");
            Player player = Bukkit.getServer().getPlayer(playerName);
            if(player == null || (!player.getName().equals(playerName))) throw new CommandExecutionException("&cPlayer not found.");
            if(flags.contains("force")) {
                portalManager.getPortal(name).executeActions(player);
            }
            else {
                portalManager.getPortal(name).trigger(player);
            }
        }
        else {
            portalManager.getPortal(name).trigger(players);
        }

        return new CommandResponse("&aPortal triggered.");
    }
}
