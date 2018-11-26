package me.iblitzkriegi.lottoplugin.runnables;

import me.iblitzkriegi.lottoplugin.LottoPlugin;
import me.iblitzkriegi.lottoplugin.util.TicketHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LotteryRunnable extends BukkitRunnable {

    private final LottoPlugin plugin;
    private int interval;
    private Date lotteryEnding;

    public LotteryRunnable(LottoPlugin plugin, int interval, Date lotteryEnding) {
        this.plugin = plugin;
        this.interval = interval;
        this.lotteryEnding = lotteryEnding;
    }

    @Override
    public void run() {
        //TODO Add admin shutdown to stop lottery on the run
        Date todaysDate = new Date();
        long difference = todaysDate.getTime() - lotteryEnding.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);
        System.out.println("here");
        System.out.println("if " + seconds + " equals " + interval);
        System.out.println("if " + seconds + " more than " + interval);
        if (seconds == interval || seconds > interval) {
            TicketHandler.endLottery();
            this.cancel();
        }

    }
}
