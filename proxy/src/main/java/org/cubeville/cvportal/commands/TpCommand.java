package org.cubeville.cvportal.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import org.cubeville.cvipc.CVIPC;

public class TpCommand extends Command
{
    CVIPC ipc;
    
    public TpCommand(CVIPC ipc) {
        super("tp", "cvportal.tp");
        this.ipc = ipc;
    }

    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof ProxiedPlayer)) return;
        ProxiedPlayer sender = (ProxiedPlayer) commandSender;

        if(args.length == 0) {
            sender.sendMessage("§c/tp <target>");
        }
        if(args.length == 1) {
            String target = args[0];
            if(target.startsWith("portal:")) {
                if(!sender.hasPermission("cvportal.tp.portal")) {
                    sender.sendMessage("§cNo permission");
                    return;
                }
                target = target.substring(7);
                String targetServer = target.substring(0, target.indexOf("|"));
                String targetPortal = target.substring(target.indexOf("|") + 1);
                sender.sendMessage("Teleport to portal " + targetPortal + " on server " + targetServer);
                ipc.sendMessage(targetServer, "xwportal|" + sender.getUniqueId() + "|portal:" + targetPortal + "|" + targetServer);
            }
            else {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(target);
                if(player == null) {
                    sender.sendMessage("§cPlayer not found.");
                    return;
                }
                
                String targetServer = player.getServer().getInfo().getName();
                String sourceServer = sender.getServer().getInfo().getName();
                if(sourceServer.equals(targetServer)) {
                    ipc.sendMessage(targetServer, "tplocal|" + sender.getUniqueId() + "|player:" + player.getUniqueId());
                }
                else {
                    ipc.sendMessage(targetServer, "xwportal|" + sender.getUniqueId() + "|player:" + player.getUniqueId() + "|" + targetServer);
                }
            }
        }
    }
}
