package org.cubeville.portal.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterDouble;
import org.cubeville.commons.commands.CommandParameterEnum;
import org.cubeville.commons.commands.CommandResponse;

import org.cubeville.portal.actions.Playsound;

import org.cubeville.portal.Portal;
import org.cubeville.portal.PortalManager;

public class PortalSetSound extends Command
{
    public PortalSetSound() {
        super("set sound");
        addBaseParameter(new CommandParameterPortal());
        addBaseParameter(new CommandParameterEnum(Sound.class));
        addOptionalBaseParameter(new CommandParameterDouble());
    }

    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters)
        throws CommandExecutionException {

        Portal portal = (Portal) baseParameters.get(0);

        float pitch = 1.0f;
        if(baseParameters.size() == 3) {
            pitch = ((Double) baseParameters.get(2)).floatValue();
            if(pitch < 0.5 || pitch > 2.0) throw new CommandExecutionException("&cPitch must be between 0.5 and 2.0");
        }
        
        Sound sound = (Sound) baseParameters.get(1);
        portal.addAction(new Playsound(sound, pitch));
        PortalManager.getInstance().save();

        return new CommandResponse("&aSound set.");
    }
}
