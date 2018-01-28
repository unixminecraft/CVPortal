package org.cubeville.cvportal.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import org.cubeville.cvportal.warps.WarpManager;

public class WarpCommand extends Command
{
    private WarpManager warpManager;
    
    public WarpCommand(WarpManager warpManager) {
        super("warp");
        this.warpManager = warpManager;
    }

    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof ProxiedPlayer)) return;
        ProxiedPlayer sender = (ProxiedPlayer) commandSender;

        if(args.length != 1) {
            sender.sendMessage("§c/warp <target>");
            return;
        }

        String target = args[0].toLowerCase();

        if(target.equals("list")) {
            String warplist = "";
            for(String warp: warpManager.getWarpNames()) {
                if(sender.hasPermission("cvportal.warp." + warp)) {
                    if(warplist.length() > 0) warplist += ", ";
                    warplist += warp;
                }
            }
            sender.sendMessage("§a" + warplist);
            return;
        }
        
        if(sender.hasPermission("cvportal.warp." + target)) {
            warpManager.teleport(sender, target);
        }
        else {
            sender.sendMessage("§cNo permission.");
        }
    }
}
