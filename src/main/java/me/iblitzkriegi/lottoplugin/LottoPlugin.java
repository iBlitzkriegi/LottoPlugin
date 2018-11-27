package me.iblitzkriegi.lottoplugin;

import me.iblitzkriegi.lottoplugin.commands.LotteryCommand;
import me.iblitzkriegi.lottoplugin.commands.TicketCommand;
import me.iblitzkriegi.lottoplugin.runnables.LotteryBroadcastRunnable;
import me.iblitzkriegi.lottoplugin.util.LotteryDataLoader;
import me.iblitzkriegi.lottoplugin.runnables.LotteryRunnable;
import me.iblitzkriegi.lottoplugin.util.TicketHandler;
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

public final class LottoPlugin extends JavaPlugin {

    public static File currentLotteryFile;
    public static FileConfiguration currentLottery;
    public static BukkitTask lotteryTask;
    public static BukkitTask lotteryBroadcastTask;
    public static String prefix;
    public static String chatFormat;
    public static String winnerMessage;
    private static LottoPlugin instance;
    //TODO Kill this static abuse ^


    public LottoPlugin() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("lottery").setExecutor(new LotteryCommand());
        this.getCommand("buyticket").setExecutor(new TicketCommand());
        getServer().getConsoleSender().sendMessage("LotteryPlugin Enabled");
        saveDefaultConfig();
        createLotteryPlayerConfig();
        new TicketHandler(this);
        LotteryDataLoader.loadTickets();
        int interval = Util.parseInterval(getConfig().getString("interval"));
        int broadcastMinutes = Util.parseInterval(getConfig().getString("broadcast-interval"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        if (getCurrentLottery().getString("end-date").equalsIgnoreCase("0") && getConfig().getBoolean("auto-start")) {
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
            lotteryBroadcastTask = new LotteryBroadcastRunnable(date).runTaskTimerAsynchronously(this, 20, 20 * broadcastMinutes);
            Date lotteryEnding = Util.parseDate(getCurrentLottery().getString("end-date"));
            lotteryTask = new LotteryRunnable(this, interval, lotteryEnding).runTaskTimerAsynchronously(this, 20, 20 * 60);

        }
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

}