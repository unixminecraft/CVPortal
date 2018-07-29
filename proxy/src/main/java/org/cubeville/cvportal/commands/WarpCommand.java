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

        if(args.length >= 1) {
            if(args[0].equals("delete")) {
                if(!commandSender.hasPermission("cvportal.warp.delete")) {
                    commandSender.sendMessage("§cNo permission.");
                    return;
                }
                if(args.length != 2) {
                    commandSender.sendMessage("§c/warp delete <warp>");
                    return;
                }
                String warp = args[1];
                if(warpManager.warpExists(warp)) {
                    warpManager.delete(warp);
                    commandSender.sendMessage("§aWarp deleted.");
                }
                else {
                    commandSender.sendMessage("§cWarp does not exist!");
                }
                return;
            }
            if(args[0].equals("rename")) {
                if(!commandSender.hasPermission("cvportal.warp.rename")) {
                    commandSender.sendMessage("§cNo permission.");
                    return;
                }
                if(args.length != 3) {
                    commandSender.sendMessage("§c/warp rename <old> <new>");
                    return;
                }
                String old = args[1];
                String neww = args[2];
                if(!warpManager.warpExists(old)) {
                    commandSender.sendMessage("§cWarp does not exist!");
                }
                else if(warpManager.warpExists(neww)) {
                    commandSender.sendMessage("§cWarp with that name already exists!");
                }
                else {
                    warpManager.rename(old, neww);
                    commandSender.sendMessage("§aWarp renamed.");
                }
                return;
            }
        }

        if(args.length == 2) {
            if(!commandSender.hasPermission("cvportal.warp.others")) {
                commandSender.sendMessage("§c/warp <target>");
                return;
            }
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
            String target = args[1].toLowerCase();
            warpManager.teleport(player, target);
            return;
        }
        
        if(args.length != 1) {
            commandSender.sendMessage("§c/warp <target>");
            return;
        }

        String target = args[0].toLowerCase();

        if(target.equals("list")) {
            List<String> warplist = new ArrayList<>();
            for(String warp: warpManager.getWarpNames()) {
                if(commandSender.hasPermission("cvportal.warp." + warp)) {
                    warplist.add(warp);
                }
            }
            Collections.sort(warplist);
            String s = "";
            for(String warp: warplist) {
                if(s.length() > 0) s += ", ";
                s += warp;
            }
            commandSender.sendMessage("§a" + s);
            return;
        }
        
        if(!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("§cOnly players can use warps.");
            return;
        }

        ProxiedPlayer sender = (ProxiedPlayer) commandSender;
        if(sender.hasPermission("cvportal.warp." + target)) {
            warpManager.teleport(sender, target);
        }
        else {
            sender.sendMessage("§cNo permission.");
        }
    }
}
