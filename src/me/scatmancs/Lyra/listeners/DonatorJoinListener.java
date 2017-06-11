package me.scatmancs.Lyra.listeners;

import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class DonatorJoinListener
  implements Listener
{
  @EventHandler
  public void onJoinRank(PlayerLoginEvent e)
  {
    Player p = e.getPlayer();
    if ((p.hasPermission("lyra.player.staff")) || (p.hasPermission("lyra.player.donator")))
    {
      if (Bukkit.getOnlinePlayers().length == Bukkit.getMaxPlayers()) {
        e.allow();
      }
    }
    else if ((!p.hasPermission("lyra.player.staff") || !p.hasPermission("lyra.player.donator")) && (Bukkit.getOnlinePlayers().length == Bukkit.getMaxPlayers()))
    {
      e.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.translateAlternateColorCodes('&', "&cThe server is currently full, buy a reserved slot @ &lstore.Lyra.com"));
      for (Player staff : Bukkit.getOnlinePlayers()) {
        if (staff.isOp()) {
          staff.sendMessage(ChatColor.RED + "You should probably increase the slots as there are people trying to log in, but the server is full");
        }
      }
    }
  }
}
