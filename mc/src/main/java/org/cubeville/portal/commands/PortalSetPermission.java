package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;

import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterBoolean;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;

public class PortalSetPermission extends BaseCommand
{
    public PortalSetPermission() {
        super("set permission");
        addBaseParameter(new CommandParameterPortal());
        addBaseParameter(new CommandParameterString());
    }

    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(0);

        portal.setPermission((String) baseParameters.get(1));
        PortalManager.getInstance().save();

        return new CommandResponse("&aPermission set.");
    }
}
