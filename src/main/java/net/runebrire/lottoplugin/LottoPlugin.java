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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class LottoPlugin extends JavaPlugin {

    public static File lotteryPlayerFile;
    public static FileConfiguration lotteryPlayer;

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
        if (getLotteryPlayerConfig().getString("date").equalsIgnoreCase("0")) {
            Calendar calendar = Calendar.getInstance();
            Date now = new Date();
            calendar.setTime(now);
            calendar.add(Calendar.SECOND, interval);
            String date = simpleDateFormat.format(calendar.getTime());
            getLotteryPlayerConfig().set("date", date);
            try {
                this.lotteryPlayer.save(this.lotteryPlayerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            getServer().broadcastMessage("A new lottery has begun! Get your tickets now with /lottery buy. The lottery will end at " + date);

        }
        Date lotteryEnding = Util.parseDate(getLotteryPlayerConfig().getString("date"));
        new LotteryRunnable(this, interval, lotteryEnding).runTaskTimerAsynchronously(this, 20, 20 * 60);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        LotteryDataLoader.saveTickets();
        getServer().getConsoleSender().sendMessage("LotteryPlugin Disabled.");
    }

    public FileConfiguration getLotteryPlayerConfig() {
        return this.lotteryPlayer;

    }

    private void createLotteryPlayerConfig() {
        lotteryPlayerFile = new File(getDataFolder(), "lottery-players.yml");
        if (!lotteryPlayerFile.exists()) {
            lotteryPlayerFile.getParentFile().mkdirs();
            saveResource("lottery-players.yml", false);
        }

        lotteryPlayer = new YamlConfiguration();
        try {
            lotteryPlayer.load(lotteryPlayerFile);
        } catch (IOException | InvalidConfigurationException x) {
            x.printStackTrace();
        }
    }

}