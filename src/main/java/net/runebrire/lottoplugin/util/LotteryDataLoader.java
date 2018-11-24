package net.runebrire.lottoplugin.util;

import net.runebrire.lottoplugin.LottoPlugin;

import java.io.IOException;

public class LotteryDataLoader {

    private static LottoPlugin plugin = TicketHandler.getPlugin();

    public static void saveTickets() {
        for (String uuid : TicketHandler.getTicketMap().keySet()) {
            plugin.getLotteryPlayerConfig().set("tickets." + uuid, TicketHandler.getTicketMap().get(uuid));

        }
        try {
            plugin.lotteryPlayer.save(plugin.lotteryPlayerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTickets() {
        if (!plugin.getLotteryPlayerConfig().contains("tickets")) return;
        for (String uuid : plugin.getLotteryPlayerConfig().getConfigurationSection("tickets").getKeys(false)) {
            TicketHandler.setTicket(uuid, plugin.getLotteryPlayerConfig().getString("tickets." + uuid));
        }
    }

}
