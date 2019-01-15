package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterInteger;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;
import org.cubeville.portal.actions.Action;

public class PortalRemoveAction extends Command {

    String className;

    public PortalRemoveAction(String command, String className) {
        super(command);
        addBaseParameter(new CommandParameterPortal());
        addBaseParameter(new CommandParameterInteger());
        this.className = className;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
            throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(0);

        int commandIndex = ((int) baseParameters.get(1)) - 1;
        try {
            if(!portal.removeActionByType((Class<? extends Action>)Class.forName("org.cubeville.portal.actions." + this.className), commandIndex)) {
                throw new CommandExecutionException("&cNo valid index for this type of action!");
            }
        }
        catch(ClassNotFoundException e) {
            System.out.println("Class not found exception");
        }
        PortalManager.getInstance().save();

        return new CommandResponse("&aAction removed.");
    }

}
