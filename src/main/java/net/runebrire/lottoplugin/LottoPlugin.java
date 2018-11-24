package net.runebrire.lottoplugin;

import net.runebrire.lottoplugin.commands.Lottery;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class LottoPlugin extends JavaPlugin {

    private File lotteryPlayerFile;
    private FileConfiguration lotteryPlayer;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("lottery").setExecutor(new Lottery());
        getServer().getConsoleSender().sendMessage("LotteryPlugin Enabled");
        saveDefaultConfig();
        createLotteryPlayerConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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