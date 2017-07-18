package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.CVPortal;

public class Setwarp extends Command
{
    public Setwarp() {
        super("setwarp");
        addBaseParameter(new CommandParameterString());
    }
    
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        String name = (String) baseParameters.get(0);
        Location location = player.getLocation();

        String message = name + "|" + location.getWorld().getName() + "," + round(location.getX()) + "," + round(location.getY()) + "," + round(location.getZ()) + "," + round(location.getPitch()) + "," + round(location.getYaw());
        CVPortal.getInstance().getCVIPC().sendMessage("setwarp", message);
        return null;
    }

    private double round(double val) {
        return val;
    }
}
