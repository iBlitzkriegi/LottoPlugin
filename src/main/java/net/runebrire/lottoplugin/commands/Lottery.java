package net.runebrire.lottoplugin.commands;

import net.runebrire.lottoplugin.util.TicketHandler;
import net.runebrire.lottoplugin.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.runebrire.lottoplugin.util.Util.sendMessage;

public class Lottery implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("You need to be a player to execute that command!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            sendMessage(player, "You need to give the command an argument");
            sendMessage(player, "/lottery buy");
            return true;
        }
        String commandName = args[0].toLowerCase();
        String uuid = Util.getUniqueId(player);
        switch (commandName) {
            case "buy":
                if (!TicketHandler.hasTicket(uuid)) {
                    if (!TicketHandler.isRunning()) {
                        sendMessage(player, "The lottery currently isnt running. ask an admin to start one or wait for the next server restart!");
                        return true;
                    }
                    TicketHandler.setTicket(uuid, "24, 24, 24, 24");
                    sendMessage(player, "Your ticets " + TicketHandler.getTicket(uuid));
                }
                break;
            case "time":
                if(Util.hasPermission(player, "lottery.time")){
                System.out.println("Has required permission");
                sendMessage(player, "There is currently time remaining");//TODO: Add a check for a timer when Lottery Ends
            }
            case "reset":
                TicketHandler.endLottery();
        }
        return true;
    }
}

//TODO: Add Admin commands for Lottery Start/Stop/Restart
