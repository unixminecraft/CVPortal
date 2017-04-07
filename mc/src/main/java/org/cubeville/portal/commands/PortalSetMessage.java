package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.commons.utils.ColorUtils;
import org.cubeville.portal.PortalManager;
import org.cubeville.portal.actions.Message;

public class PortalSetMessage extends Command
{
    public PortalSetMessage() {
        super("set message");
        addBaseParameter(new CommandParameterString());
        addBaseParameter(new CommandParameterString());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        String name = (String) baseParameters.get(0);
        PortalManager portalManager = PortalManager.getInstance();
        if(portalManager.getPortal(name) == null) throw new CommandExecutionException("&cPortal does not exist!");

        String message = ColorUtils.addColor((String) baseParameters.get(1));
        portalManager.getPortal(name).addAction(new Message(message));
        portalManager.save();

        return new CommandResponse("&aMessage set.");        
    }
}
