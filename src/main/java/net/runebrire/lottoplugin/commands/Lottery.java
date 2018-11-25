package net.runebrire.lottoplugin.commands;


import net.runebrire.lottoplugin.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Lottery implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("You need to be a player to execute that command!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            Util.sendMessage(player, "You need to give the command an argument");
            Util.sendMessage(player, "/lottery buy");
            return true;
        }
        String commandName = args[0].toLowerCase();
        switch (commandName) {
            case "buy":
                // Check if User has permission to execute /lottery buy command to purchase a ticket
                if(Util.hasPermission(player, "lottery.buy")){
                    System.out.println("That user has the required permission!");
                    //Upon purchase, creates a ticket and shows user the ticket numbers
            }
                break;
            case "time":
                if(Util.hasPermission(player, "lottery.time")){
                System.out.println("Has required permission");
                Util.sendMessage(player, "There is currently time remaining");//TODO: Add a check for a timer when Lottery Ends
            }
        }
        return true;
    }
}

//TODO: Add Admin commands for Lottery Start/Stop/Restart
