package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandParameterUUID;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.LoginTeleporter;

public class PortalLoginTarget extends Command
{
    LoginTeleporter loginTeleporter;
    
    public PortalLoginTarget(LoginTeleporter loginTeleporter) {
        super("logintarget");
        addBaseParameter(new CommandParameterUUID());
        addBaseParameter(new CommandParameterString());
        this.loginTeleporter = loginTeleporter;
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        UUID playerId = (UUID) baseParameters.get(0);
        String location = (String) baseParameters.get(1);

        loginTeleporter.setLoginTarget(playerId, "portal:" + location);
        return null;
    }
}
