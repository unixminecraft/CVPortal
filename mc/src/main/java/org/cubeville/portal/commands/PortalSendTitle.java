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
import org.cubeville.commons.commands.CommandParameterInteger;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.Portal;

public class PortalSendTitle extends BaseCommand
{
    public PortalSendTitle() {
        super("sendtitle");
        addBaseParameter(new CommandParameterPortal());
        addBaseParameter(new CommandParameterString());
        addBaseParameter(new CommandParameterString());
        addBaseParameter(new CommandParameterInteger());
        addBaseParameter(new CommandParameterInteger());
        addBaseParameter(new CommandParameterInteger());
    }

    @SuppressWarnings("unchecked")
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(0);

        Collection<Player> players = (Collection<Player>) Bukkit.getServer().getOnlinePlayers();
        String title = ColorUtils.addColor((String) baseParameters.get(1));
        String subtitle = ColorUtils.addColor((String) baseParameters.get(2));
        int fadeIn = (Integer) baseParameters.get(3);
        int stay = (Integer) baseParameters.get(4);
        int fadeOut = (Integer) baseParameters.get(5);
        
        portal.sendTitle(players, title, subtitle, fadeIn, stay, fadeOut);

        return new CommandResponse("&aTitle sent.");
    }
        
}
