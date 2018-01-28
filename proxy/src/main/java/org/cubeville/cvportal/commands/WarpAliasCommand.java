package org.cubeville.cvportal.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import org.cubeville.cvportal.warps.WarpManager;

public class WarpAliasCommand extends Command
{
    private WarpManager warpManager;
    private String target;

    public WarpAliasCommand(WarpManager warpManager, String command, String target) {
        super(command, "cvportal.warp." + target);
        this.warpManager = warpManager;
        this.target = target;
    }

    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof ProxiedPlayer)) return;
        ProxiedPlayer sender = (ProxiedPlayer) commandSender;

        if(args.length > 0) {
            sender.sendMessage("§cToo many arguments.");
            return;
        }

        warpManager.teleport(sender, target);
    }
}
