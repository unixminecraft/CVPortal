package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;

import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterBoolean;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;

public class PortalSetPermanent extends BaseCommand
{
    public PortalSetPermanent() {
        super("set permanent");
        addBaseParameter(new CommandParameterPortal());
        addBaseParameter(new CommandParameterBoolean());
    }

    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(0);

        portal.setPermanent((boolean) baseParameters.get(1));
        PortalManager.getInstance().save();
        
        return new CommandResponse("&aValue set.");
    }
}
