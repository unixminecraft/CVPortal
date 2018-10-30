package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandParameterDouble;
import org.cubeville.commons.commands.CommandResponse;

public class Tppos extends Command
{
    public Tppos() {
        super("");
        addBaseParameter(new CommandParameterDouble());
        addBaseParameter(new CommandParameterDouble());
        addBaseParameter(new CommandParameterDouble());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) {
        double x = (double) baseParameters.get(0);
        double y = (double) baseParameters.get(1);
        double z = (double) baseParameters.get(2);

        System.out.println("Teleport " + player.getName() + " to " + x + "/" + y + "/" + z);
        player.teleport(new Location(player.getLocation().getWorld(), x, y, z));

        return null;
    }
}
