package net.runebrire.lottoplugin.util;

import net.runebrire.lottoplugin.LottoPlugin;

import java.util.Arrays;
import java.util.HashMap;

public class TicketHandler {
    public static HashMap<String, String> tickets = new HashMap<>(); //PlayerUUID, The string containing their ticket stub
    private static LottoPlugin plugin;

    public TicketHandler(LottoPlugin instance) {
        plugin = instance;
    }

    public static void setTicket(String uuid, String generatedTicket) { tickets.put(uuid, generatedTicket);
    }

    public static String getTicket(String uuid) {
        return tickets.get(uuid);
    }

    public static boolean hasTicket(String uuid) {
        return tickets.containsKey(uuid);
    }

    public static HashMap<String, String> getTicketMap() {
        return tickets;
    }

    public static LottoPlugin getPlugin() {
        return plugin;
    }

    public static boolean ticketExists(String ticket) {
        for (String savedTicket : getTicketMap().values()) {
            if (savedTicket.equalsIgnoreCase(ticket)) {
                return true;
            }
        }
        return false;

    }

}
