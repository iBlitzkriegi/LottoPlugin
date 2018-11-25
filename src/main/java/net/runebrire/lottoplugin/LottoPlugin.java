package net.runebrire.lottoplugin;

import net.runebrire.lottoplugin.commands.Lottery;
import net.runebrire.lottoplugin.util.LotteryDataLoader;
import net.runebrire.lottoplugin.util.LotteryRunnable;
import net.runebrire.lottoplugin.util.TicketHandler;
import net.runebrire.lottoplugin.util.Util;
import org.bukkit.Bukkit;
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
            getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
                String[] timeAndDate = date.split(" ");
                getServer().broadcastMessage("A new lottery has begun! Get your tickets now with /lottery buy. The lottery will end on " + timeAndDate[0] + " at " + timeAndDate[1]);
            }, 20, (20 * 60) * 10);

        }
        Date lotteryEnding = Util.parseDate(getCurrentLottery().getString("end-date"));
        BukkitTask task = new LotteryRunnable(this, interval, lotteryEnding).runTaskTimerAsynchronously(this, 20, 20 * 60);

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