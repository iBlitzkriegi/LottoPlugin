package me.iblitzkriegi.lottoplugin.util;

import me.iblitzkriegi.lottoplugin.LottoPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

public class LotteryDataLoader {

    private static LottoPlugin plugin = TicketHandler.getPlugin();

    public static void saveTickets() {
        if (TicketHandler.getTicketMap().isEmpty()) return;
        for (String uuid : TicketHandler.getTicketMap().keySet()) {
            plugin.getCurrentLottery().set("tickets." + uuid, TicketHandler.getTicketMap().get(uuid));
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
            TicketHandler.setTicket(uuid, currentLottery.getString("tickets." + uuid));
        }
    }

}
