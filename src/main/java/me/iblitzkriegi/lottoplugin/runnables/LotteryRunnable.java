package me.iblitzkriegi.lottoplugin.runnables;

import me.iblitzkriegi.lottoplugin.LottoPlugin;
import me.iblitzkriegi.lottoplugin.util.LotteryHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LotteryRunnable extends BukkitRunnable {

    private final LottoPlugin plugin;
    private long interval;
    private Date lotteryEnding;

    public LotteryRunnable(LottoPlugin plugin, long interval, Date lotteryEnding) {
        this.plugin = plugin;
        this.interval = interval;
        this.lotteryEnding = lotteryEnding;
    }

    @Override
    public void run() {
        if (interval == 0) {
            Date todaysDate = new Date();
            if (todaysDate.after(lotteryEnding)) {
                LotteryHandler.endLottery();
                this.cancel();
            }
        } else {
            Date todaysDate = new Date();
            long difference = todaysDate.getTime() - lotteryEnding.getTime();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);
            if (seconds == interval || seconds > interval) {
                LotteryHandler.endLottery();
                this.cancel();
            }
        }

    }
}
