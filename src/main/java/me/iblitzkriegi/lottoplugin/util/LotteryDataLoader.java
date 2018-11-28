package me.iblitzkriegi.lottoplugin.util;

import me.iblitzkriegi.lottoplugin.LottoPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class LotteryDataLoader {

    private static LottoPlugin plugin = LotteryHandler.getPlugin();

    public static void saveTickets() {
        if (LotteryHandler.getTicketMap().isEmpty()) return;
        for (String uuid : LotteryHandler.getTicketMap().keySet()) {
            plugin.getCurrentLottery().set("tickets." + uuid, LotteryHandler.getTicketMap().get(uuid));
        }
        try {
            plugin.currentLottery.save(plugin.currentLotteryFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTickets() {
        if (!plugin.getCurrentLottery().contains("tickets")) return;
        FileConfiguration currentLottery = plugin.getCurrentLottery();
        for (String uuid : currentLottery.getConfigurationSection("tickets").getKeys(false)) {
            LotteryHandler.setTicket(uuid, currentLottery.getString("tickets." + uuid));
        }
    }

}
