package me.iblitzkriegi.lottoplugin.runnables;

import me.iblitzkriegi.lottoplugin.util.TicketHandler;
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
        if (!TicketHandler.hasTicket(uuid)) {
            String ticket = Util.formatTicket(Util.getRandomNumber(), Util.getRandomNumber(), Util.getRandomNumber(), Util.getRandomNumber());
            if (!TicketHandler.ticketExists(ticket)) {
                Util.sendMessage(player, "Success! Your lottery ticket is: " + ticket);
                TicketHandler.setTicket(uuid, ticket);
                this.cancel();
            }
        } else {
            Util.sendMessage(player, "Success! Your lottery ticket is: " + TicketHandler.getTicket(uuid));
            this.cancel();
        }

    }

}
