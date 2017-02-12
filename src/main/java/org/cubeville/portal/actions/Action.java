package org.cubeville.portal.actions;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import org.bukkit.entity.Player;

public interface Action extends ConfigurationSerializable
{
    public void execute(Player player);
    public String getShortInfo();
}
