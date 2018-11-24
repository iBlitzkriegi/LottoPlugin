package net.runebrire.lottoplugin.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Util {
    public static void sendMessage(CommandSender commandSender, String message) {
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
