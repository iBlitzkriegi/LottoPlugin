package net.runebrire.lottoplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class lottery implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length > 0){
                if(args[0].equalsIgnoreCase("buy")){
                    player.sendMessage("You bought a ticket!");
                } else if(args[0].equalsIgnoreCase("other")){
                    player.sendMessage("Not really useful just testing.");
                }
            }else{
                player.sendMessage("You need to give the command an argument");
                player.sendMessage("/lottery buy");
            }
        }else{
            System.out.println("You need to be a player to execute that command!");
        }
        return false;
    }
}
