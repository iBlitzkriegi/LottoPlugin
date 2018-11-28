package me.iblitzkriegi.lottoplugin.runnables;

import me.iblitzkriegi.lottoplugin.util.LotteryHandler;
import me.iblitzkriegi.lottoplugin.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TicketCreaterRunnable extends BukkitRunnable {

    private Player player;

    public TicketCreaterRunnable(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        String uuid = Util.getUniqueId(player);
        if (!LotteryHandler.hasTicket(uuid)) {
            String ticket = Util.formatTicket(Util.getRandomNumber(), Util.getRandomNumber(), Util.getRandomNumber(), Util.getRandomNumber());
            if (!LotteryHandler.ticketExists(ticket)) {
                Util.sendMessage(player, "Success! Your lottery ticket is: " + ticket);
                LotteryHandler.setTicket(uuid, ticket);
                this.cancel();
            }
        } else {
            Util.sendMessage(player, "Success! Your lottery ticket is: " + LotteryHandler.getTicket(uuid));
            this.cancel();
        }

    }

}
