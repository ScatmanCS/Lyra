package me.scatmancs.Lyra.fixes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HitDelayFix
  implements Listener
{
  @EventHandler
  public void onJoin(PlayerJoinEvent event)
  {
    event.getPlayer().setMaximumNoDamageTicks(19);
  }
}
