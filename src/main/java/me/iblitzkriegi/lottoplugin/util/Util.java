package me.iblitzkriegi.lottoplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Util {
    public static boolean hasPermission(Player player, String permission) {
        if (player.hasPermission(permission)) {
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


    public static Date parseDate(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getNameFromUUID(String input) {
        UUID uuid = UUID.fromString(input);
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if(player == null) return null;
        return player.getName();
    }

    public static void broadcastMessage(String s) {
        //TODO Add a config option to create a chat format aka prefix
        Bukkit.getServer().broadcastMessage(s);
    }


}
