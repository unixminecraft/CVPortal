package org.cubeville.portal.commands;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.cubeville.commons.utils.ColorUtils;
import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;

public class PortalSendMessage extends BaseCommand
{
    public PortalSendMessage() {
        super("sendmessage");
        addBaseParameter(new CommandParameterPortal());
        addBaseParameter(new CommandParameterString());
    }

    @SuppressWarnings("unchecked")
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(0);

        Collection<Player> players = (Collection<Player>) Bukkit.getServer().getOnlinePlayers();
        String message = ColorUtils.addColor((String) baseParameters.get(1));
        portal.sendMessage(players, message);

        return new CommandResponse("&aMessage sent.");
    }

}
