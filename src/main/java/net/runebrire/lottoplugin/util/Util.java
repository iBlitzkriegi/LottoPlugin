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

    public static Integer parseInterval(String s) {
        if (s.endsWith("d")) {
            return Integer.valueOf(s.replaceFirst("d", "")) * 86400;
        } else if (s.endsWith("mo")) {
            return Integer.valueOf(s.replaceFirst("mo", "")) * 2678400;
        } else if (s.endsWith("hr")) {
            return Integer.valueOf(s.replaceFirst("hr", "")) * 3600;
        } else if (s.endsWith("m")) {
            return Integer.valueOf(s.replaceFirst("m", "")) * 60;
        }
        return null;
    }


}
