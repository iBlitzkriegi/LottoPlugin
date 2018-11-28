package me.iblitzkriegi.lottoplugin.commands;

import me.iblitzkriegi.lottoplugin.LottoPlugin;
import me.iblitzkriegi.lottoplugin.runnables.TicketCreaterRunnable;
import me.iblitzkriegi.lottoplugin.util.TicketHandler;
import me.iblitzkriegi.lottoplugin.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TicketCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            Util.sendMessage(commandSender, "You must be a player in order to purchase a ticket!");
            return true;
        }
        Player player = (Player) commandSender;
        // The hasPermission method already handles the error if they don't have the permission
        if (!Util.hasPermission(player, "lottery.buy")) {
            return true;
        }
        if (!TicketHandler.isRunning()) {
            Util.sendMessage(player, "The lottery is not currently running. Ask an administrator to start one!");
            return true;
        }
        String uuid = Util.getUniqueId(player);
        if (TicketHandler.hasTicket(uuid)) {
            Util.sendMessage(player, "You already have a ticket! You must wait until this lottery is over to purchase another ticket.");
            return true;
        }

        new TicketCreaterRunnable(player).runTaskTimerAsynchronously(LottoPlugin.getInstance(), 0, 20);
        return true;
    }
}
