package org.cubeville.portal.commands;

import org.cubeville.commons.commands.CommandParameterDynamicallyEnumeratedObject;

import org.cubeville.portal.PortalManager;

public class CommandParameterPortal extends CommandParameterDynamicallyEnumeratedObject
{
    public CommandParameterPortal() {
        super(PortalManager.getInstance());
    }
}
