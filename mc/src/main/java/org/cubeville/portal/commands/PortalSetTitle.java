package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterInteger;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.commons.utils.ColorUtils;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;
import org.cubeville.portal.actions.Title;

public class PortalSetTitle extends Command
{
    public PortalSetTitle() {
        super("set title");
        addBaseParameter(new CommandParameterPortal());
        addBaseParameter(new CommandParameterString());
        addBaseParameter(new CommandParameterString());
        addBaseParameter(new CommandParameterInteger());
        addBaseParameter(new CommandParameterInteger());
        addBaseParameter(new CommandParameterInteger());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(0);

        String title = (String) baseParameters.get(1);
        title = ColorUtils.addColor(title);
        String subtitle = (String) baseParameters.get(2);
        subtitle = ColorUtils.addColor(subtitle);
        int fadeIn = (Integer) baseParameters.get(3);
        int stay = (Integer) baseParameters.get(4);
        int fadeOut = (Integer) baseParameters.get(5);

        portal.addAction(new Title(title, subtitle, fadeIn, stay, fadeOut));
        PortalManager.getInstance().save();

        return new CommandResponse("&aTitle set.");
    }
}
