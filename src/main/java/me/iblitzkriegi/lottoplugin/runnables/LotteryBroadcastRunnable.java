package me.iblitzkriegi.lottoplugin.runnables;

import me.iblitzkriegi.lottoplugin.util.TicketHandler;
import me.iblitzkriegi.lottoplugin.util.Util;
import org.bukkit.scheduler.BukkitRunnable;

public class LotteryBroadcastRunnable extends BukkitRunnable {

    private String date;

    public LotteryBroadcastRunnable(String date) {
        this.date = date;
    }

    @Override
    public void run() {
        if (TicketHandler.isRunning()) {
            String[] timeAndDate = date.split(" ");
            Util.broadcastMessage("A new lottery has begun! Get your tickets now with /lottery buy. The lottery will end on " + timeAndDate[0] + " at " + timeAndDate[1]);
        } else {
            this.cancel();
        }
    }
}
