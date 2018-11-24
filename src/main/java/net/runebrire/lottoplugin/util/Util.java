package net.runebrire.lottoplugin.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util {
    public static boolean hasPermission(Player player, String permission){
        if(player.hasPermission(permission)){
            return true;
        }
        sendMessage(player, "You must have the " + permission + " permission in order to run this command!");
        return false;
    }

    public static void sendMessage(CommandSender commandSender, String message) {
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static String getUniqueId(Player player) {
        return player.getUniqueId().toString();
    }

}
