package me.iblitzkriegi.lottoplugin;

import me.iblitzkriegi.lottoplugin.commands.Lottery;
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

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("lottery").setExecutor(new Lottery());
        getServer().getConsoleSender().sendMessage("LotteryPlugin Enabled");
        saveDefaultConfig();
        createLotteryPlayerConfig();
        new TicketHandler(this);
        LotteryDataLoader.loadTickets();
        int interval = Util.parseInterval(getConfig().getString("interval"));
        int broadcastMinutes = Util.parseInterval(getConfig().getString("broadcast-interval"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        if (getCurrentLottery().getString("end-date").equalsIgnoreCase("0")) {
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

        }
        Date lotteryEnding = Util.parseDate(getCurrentLottery().getString("end-date"));
        lotteryTask = new LotteryRunnable(this, interval, lotteryEnding).runTaskTimerAsynchronously(this, 20, 20 * 180);
        prefix = getConfig().getString("chat-format").replaceAll("%message%", "");

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

    private void createLotteryPlayerConfig() {
        currentLotteryFile = new File(getDataFolder(), "current-lottery.yml");
        if (!currentLotteryFile.exists()) {
            currentLotteryFile.getParentFile().mkdirs();
            saveResource("current-lottery.yml", false);
        }

        currentLottery = new YamlConfiguration();
        try {
            currentLottery.load(currentLotteryFile);
        } catch (IOException | InvalidConfigurationException x) {
            x.printStackTrace();
        }
    }

}