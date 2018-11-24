package net.runebrire.lottoplugin;

import net.runebrire.lottoplugin.commands.Lottery;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class LottoPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Plugin is now Loading");
        if(!loadConfig()){
            getLogger().severe("Unable to load config files. Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        this.getCommand("lottery").setExecutor(new Lottery());
        getServer().getConsoleSender().sendMessage("LotteryPlugin Enabled");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage("LotteryPlugin Disabled.");
    }

    private boolean loadConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Created config.yml");
                saveDefaultConfig();
            }
            return true;
        } catch (Exception unknownError) {
            getLogger().info("Unable to load config.yml");
            unknownError.printStackTrace();
            return false;
        }
    }
}