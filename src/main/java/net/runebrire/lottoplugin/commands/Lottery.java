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
                Util.sendMessage(player, "You bought a ticket!");
                break;
            case "other":
                Util.sendMessage(player, "You did something else!");
        }
        return true;
    }
}
