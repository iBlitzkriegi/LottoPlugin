package me.iblitzkriegi.lottoplugin.util;

import me.iblitzkriegi.lottoplugin.LottoPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TicketHandler {
    public static HashMap<String, String> tickets = new HashMap<>(); //PlayerUUID, The string containing their ticket stub
    public static HashMap<String, String> reverseTickets = new HashMap<>(); //The string containing their ticket stub, PlayerUUID
    private static LottoPlugin plugin;

    public TicketHandler(LottoPlugin instance) {
        plugin = instance;
    }

    public static void setTicket(String uuid, String generatedTicket) {
        tickets.put(uuid, generatedTicket);
        reverseTickets.put(generatedTicket, uuid);
    }

    public static String getTicket(String uuid) {
        return tickets.get(uuid);
    }

    public static boolean hasTicket(String uuid) {
        return tickets.containsKey(uuid);
    }

    public static HashMap<String, String> getTicketMap() {
        return tickets;
    }

    public static LottoPlugin getPlugin() {
        return plugin;
    }

    public static boolean ticketExists(String ticket) {
        for (String savedTicket : getTicketMap().values()) {
            if (savedTicket.equalsIgnoreCase(ticket)) {
                return true;
            }
        }
        return false;
    }

    public static HashMap<String, String> getReversedTicketMap() {
        return reverseTickets;
    }

    public static void endLottery() {
        List<String> tickets = new ArrayList<>(reverseTickets.keySet());
        if (tickets.size() == 0) {
            Util.broadcastMessage("The lottery has ended without any tickets being purchased :(, make sure you get in on the next one!");
            return;
        }
        // Pick a winner
        int randomTicket = new Random().nextInt(tickets.size());
        String winningTicket = tickets.get(randomTicket);
        String winner = Util.getNameFromUUID(reverseTickets.get(winningTicket));
        getPlugin().getServer().broadcastMessage(winner + " has won! Their ticket was " + winningTicket);
        // Code to fully reset the lottery
        getPlugin().getCurrentLottery().set("end-date", "0");
        TicketHandler.reverseTickets.clear();
        TicketHandler.tickets.clear();
        getPlugin().currentLotteryFile.delete();
        LottoPlugin.currentLotteryFile.getParentFile().mkdirs();
        getPlugin().saveResource("current-lottery.yml", true);
        getPlugin().lotteryTask.cancel();
    }

    public static boolean isRunning() {
        return getPlugin().getCurrentLottery().getString("end-date").equalsIgnoreCase("0") ? false : true;
    }

}
