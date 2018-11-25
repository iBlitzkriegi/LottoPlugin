package net.runebrire.lottoplugin.util;

import net.runebrire.lottoplugin.LottoPlugin;
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
        long difference = lotteryEnding.getTime() - todaysDate.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);
        if (seconds == interval || seconds > interval) {
            plugin.getServer().broadcastMessage("The lottery has come to an end!");
            this.cancel();
        }

    }
}
