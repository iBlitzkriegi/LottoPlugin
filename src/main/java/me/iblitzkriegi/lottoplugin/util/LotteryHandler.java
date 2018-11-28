package me.iblitzkriegi.lottoplugin.util;

import me.iblitzkriegi.lottoplugin.LottoPlugin;
import me.iblitzkriegi.lottoplugin.runnables.LotteryBroadcastRunnable;
import me.iblitzkriegi.lottoplugin.runnables.LotteryRunnable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.iblitzkriegi.lottoplugin.LottoPlugin.broadcastMinutes;
import static me.iblitzkriegi.lottoplugin.LottoPlugin.createLotteryPlayerConfig;

public class LotteryHandler {
    public static HashMap<String, String> tickets = new HashMap<>(); //PlayerUUID, The string containing their ticket stub
    public static HashMap<String, String> reverseTickets = new HashMap<>(); //The string containing their ticket stub, PlayerUUID
    private static LottoPlugin plugin;

    public LotteryHandler(LottoPlugin instance) {
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

    public static boolean isRunning() {
        return getPlugin().getCurrentLottery().getString("end-date").equalsIgnoreCase("0") ? false : true;
    }

    public static HashMap<String, String> getReversedTicketMap() {
        return reverseTickets;
    }

    public static void endLottery() {
        List<String> tickets = new ArrayList<>(reverseTickets.keySet());
        if (tickets.size() != 0) {
            int randomTicket = new Random().nextInt(tickets.size());
            String winningTicket = tickets.get(randomTicket);
            String winner = Util.getNameFromUUID(reverseTickets.get(winningTicket));
            Util.broadcastWinner(winner);
        } else {
            Util.broadcastMessage("The lottery has ended without any tickets being purchased :(, make sure you get in on the next one!");
        }
        if (getPlugin().lotteryTask != null) {
            getPlugin().getLotteryTask().cancel();
        }
        LotteryHandler.reverseTickets.clear();
        LotteryHandler.tickets.clear();
        getPlugin().currentLotteryFile.delete();
        createLotteryPlayerConfig();
    }

    public static void startLottery(String interval, String ticketPrice, String pullPrice) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        int parsedInterval = Util.parseInterval(interval);
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, parsedInterval);
        String date = simpleDateFormat.format(calendar.getTime());
        getPlugin().getCurrentLottery().set("end-date", date);
        getPlugin().getCurrentLottery().set("ticket-price", ticketPrice);
        getPlugin().getCurrentLottery().set("lottery-pull", pullPrice);
        try {
            getPlugin().currentLottery.save(getPlugin().currentLotteryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getPlugin().setLotteryBroadcastTask(new LotteryBroadcastRunnable(date).runTaskTimerAsynchronously(getPlugin(), 20, 20 * broadcastMinutes));
        Date lotteryEnding = Util.parseDate(getPlugin().getCurrentLottery().getString("end-date"));
        getPlugin().setLotteryTask(new LotteryRunnable(getPlugin(), parsedInterval, lotteryEnding).runTaskTimerAsynchronously(getPlugin(), 20, 20 * 60));
    }


}
