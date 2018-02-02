package org.cubeville.cvportal.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        if(args.length >= 1) {
            if(args[0].equals("delete")) {
                if(args.length != 2) {
                    sender.sendMessage("§c/warp delete <warp>");
                    return;
                }
                String warp = args[1];
                if(warpManager.warpExists(warp)) {
                    warpManager.delete(warp);
                    sender.sendMessage("§aWarp deleted.");
                }
                else {
                    sender.sendMessage("§cWarp does not exist!");
                }
                return;
            }
        }
        
        if(args.length != 1) {
            sender.sendMessage("§c/warp <target>");
            return;
        }

        String target = args[0].toLowerCase();

        if(target.equals("list")) {
            List<String> warplist = new ArrayList<>();
            for(String warp: warpManager.getWarpNames()) {
                if(sender.hasPermission("cvportal.warp." + warp)) {
                    warplist.add(warp);
                }
            }
            Collections.sort(warplist);
            String s = "";
            for(String warp: warplist) {
                if(s.length() > 0) s += ", ";
                s += warp;
            }
            sender.sendMessage("§a" + s);
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
