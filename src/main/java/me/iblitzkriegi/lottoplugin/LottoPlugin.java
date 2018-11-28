package me.iblitzkriegi.lottoplugin;

import me.iblitzkriegi.lottoplugin.commands.LotteryCommand;
import me.iblitzkriegi.lottoplugin.commands.TicketCommand;
import me.iblitzkriegi.lottoplugin.runnables.LotteryBroadcastRunnable;
import me.iblitzkriegi.lottoplugin.runnables.LotteryRunnable;
import me.iblitzkriegi.lottoplugin.util.LotteryDataLoader;
import me.iblitzkriegi.lottoplugin.util.LotteryHandler;
import me.iblitzkriegi.lottoplugin.util.Util;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class LottoPlugin extends JavaPlugin {

    public static File currentLotteryFile;
    public static FileConfiguration currentLottery;
    public static String prefix;
    public static String chatFormat;
    public static String winnerMessage;
    public static int broadcastMinutes;
    private static LottoPlugin instance;
    public BukkitTask lotteryTask;
    public BukkitTask lotteryBroadcastTask;
    //TODO Kill this static abuse ^


    public LottoPlugin() {
        instance = this;
    }

    public static LottoPlugin getInstance() {
        return instance;
    }

    public static void createLotteryPlayerConfig() {
        currentLotteryFile = new File(getInstance().getDataFolder(), "current-lottery.yml");
        if (!currentLotteryFile.exists()) {
            currentLotteryFile.getParentFile().mkdirs();
            getInstance().saveResource("current-lottery.yml", false);
        }

        currentLottery = new YamlConfiguration();
        try {
            currentLottery.load(currentLotteryFile);
        } catch (IOException | InvalidConfigurationException x) {
            x.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("lottery").setExecutor(new LotteryCommand());
        this.getCommand("buyticket").setExecutor(new TicketCommand());
        getServer().getConsoleSender().sendMessage("LotteryPlugin Enabled");
        saveDefaultConfig();
        createLotteryPlayerConfig();
        new LotteryHandler(this);
        LotteryDataLoader.loadTickets();
        int interval = Util.parseInterval(getConfig().getString("interval"));
        broadcastMinutes = Util.parseInterval(getConfig().getString("broadcast-interval"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String endDate = getCurrentLottery().getString("end-date");
        if (endDate.equalsIgnoreCase("0") && getConfig().getBoolean("auto-start")) {
            Calendar calendar = Calendar.getInstance();
            Date now = new Date();
            calendar.setTime(now);
            calendar.add(Calendar.SECOND, interval);
            String date = simpleDateFormat.format(calendar.getTime());
            getCurrentLottery().set("end-date", date);
            try {
                this.currentLottery.save(this.currentLotteryFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            setLotteryBroadcastTask(new LotteryBroadcastRunnable(date).runTaskTimerAsynchronously(this, 20, 20 * broadcastMinutes));
            Date lotteryEnding = Util.parseDate(getCurrentLottery().getString("end-date"));
            setLotteryTask(new LotteryRunnable(this, interval, lotteryEnding).runTaskTimerAsynchronously(this, 20, 20 * 60));

        } else if (!endDate.equalsIgnoreCase("0")) {
            setLotteryBroadcastTask(new LotteryBroadcastRunnable(endDate).runTaskTimerAsynchronously(this, 20, 20 * broadcastMinutes));
            setLotteryTask(new LotteryRunnable(this, 0, Util.parseDate(endDate)).runTaskTimerAsynchronously(this, 20, 20 * 60));
        }
        //TODO Parse the running lottery?
        prefix = getConfig().getString("plugin-prefix");
        chatFormat = getConfig().getString("chat-format").replaceFirst("%message%", "");
        winnerMessage = getConfig().getString("win-lottery-message");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        LotteryDataLoader.saveTickets();
        getServer().getConsoleSender().sendMessage("LotteryPlugin Disabled.");
    }

    public FileConfiguration getCurrentLottery() {
        return this.currentLottery;

    }

    public BukkitTask getLotteryBroadcastTask() {
        return lotteryBroadcastTask;
    }

    public void setLotteryBroadcastTask(BukkitTask task) {
        if (this.lotteryBroadcastTask != null) {
            this.lotteryBroadcastTask.cancel();
        }
        this.lotteryBroadcastTask = task;
    }

    public void setLotteryTask(BukkitTask task) {
        if (this.lotteryTask != null) {
            this.lotteryTask.cancel();
        }
        this.lotteryTask = task;
    }

    public BukkitTask getLotteryTask() {
        return lotteryTask;
    }

}