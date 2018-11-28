package me.iblitzkriegi.lottoplugin.commands;

import me.iblitzkriegi.lottoplugin.LottoPlugin;
import me.iblitzkriegi.lottoplugin.util.LotteryHandler;
import me.iblitzkriegi.lottoplugin.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LotteryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            Util.sendMessage(commandSender, "You must be a player to perform this command");
            return true;
        }
        Player player = (Player) commandSender;
        if (!Util.hasPermission(player, "lottery.admin")) {
            if (LotteryHandler.isRunning()) {
                String[] endDate = LottoPlugin.getInstance().getCurrentLottery().getString("end-date").split(" ");
                Util.sendMessage(player, "The lottery will end on " + endDate[0] + " at " + endDate[1]);
            } else {
                Util.sendMessage(player, "The lottery is not currently running. Ask an administrator to start one!");
            }
            return true;
        }
        if (args.length == 0) {
            sendHelpMessage(player);
            return true;
        }
        String commandName = args[0].toLowerCase();
        switch (commandName) {
            case "end":
            case "reset":
                if (LotteryHandler.isRunning()) {
                    LotteryHandler.endLottery();
                    return true;
                }
                Util.sendMessage(player, "The lottery is not currently running! See /lottery to see how to start one.");
                return true;
            case "start":
                if (LotteryHandler.isRunning()) {
                    Util.sendMessage(player, "There is already currently a lottery running! Use /lottery end or /lottery reset to stop it.");
                    return true;
                }
                if (args.length != 4) {
                    Util.sendMessage(player, "You must include all of the required arguments!");
                    Util.sendMessage(player, "Usage: /lottery interval ticketPrice lotteryPull");
                    return true;
                }
                String interval = args[1];
                if (interval.contains("d") || interval.contains("hr") || interval.contains("mo") || interval.contains("m")) {
                    LotteryHandler.startLottery(args[1], args[2], args[3]);
                    return true;
                } else {
                    Util.sendMessage(player, "Invalid usage. Refer to the config.yml to see how to specify an interval to start a lottery.");
                }
                return true;
            default:
                sendHelpMessage(player);
        }
        return true;
    }

    private void sendHelpMessage(Player player) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("&b&l-------- &fLottery Help &b&l---------------------\n");
        stringBuilder.append("&8Use /lottery [argument] to use each listed item\n");
        stringBuilder.append("&bReset: &fPicks a new winner and ends the lottery\n");
        stringBuilder.append("&bEnd: &fPicks a new winner and ends the lottery\n");
        stringBuilder.append("&bStart: &fMust include interval, start price, lottery pull\n");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', stringBuilder.toString()));
    }
}

