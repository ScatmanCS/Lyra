package me.scatmancs.Lyra.fixes;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class AntiPEXCrash implements Listener {
    @EventHandler(
        ignoreCancelled = true,
        priority        = EventPriority.HIGHEST
    )
    public void onPreProcess(PlayerCommandPreprocessEvent event) {
        if (((event.getMessage().toLowerCase().startsWith("/pex"))
                || (event.getMessage().toLowerCase().startsWith("/kill"))
                || (event.getMessage().toLowerCase().startsWith("/slay"))
                || (event.getMessage().toLowerCase().startsWith("/bukkit:version"))
                || (event.getMessage().toLowerCase().startsWith("/bukkit:about"))
                || (event.getMessage().toLowerCase().startsWith("/bukkit:kill"))
                || (event.getMessage().toLowerCase().startsWith("/bukkit:slay"))
                || (event.getMessage().toLowerCase().startsWith("/suicide")))
                && (!event.getPlayer().hasPermission("*.**"))) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "No permission.");
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
