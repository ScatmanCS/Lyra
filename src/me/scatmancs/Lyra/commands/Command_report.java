package me.scatmancs.Lyra.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.scatmancs.Lyra.handlers.ColorHandler;
import me.scatmancs.Lyra.utils.MessageClick;
import me.scatmancs.Lyra.utils.TimeUtils;
import me.scatmancs.Lyra.utils.Utilities;


public class Command_report
  implements CommandExecutor
{
  private final Utilities utilities = Utilities.getInstance();
  private final Map<UUID, Long> report = new HashMap();
  
  public void setCooldown(Player player, long value)
  {
    this.report.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis() + value));
  }
  
  public void removeCooldown(Player player)
  {
    if (isCooldownActive(player)) {
      this.report.remove(player.getUniqueId());
    }
  }
  
  public boolean isCooldownActive(Player player)
  {
    if (!this.report.containsKey(player.getUniqueId())) {
      return false;
    }
    return ((Long)this.report.get(player.getUniqueId())).longValue() > System.currentTimeMillis();
  }
  
  public long getMillisecondLeft(Player player)
  {
    if (!isCooldownActive(player)) {
      return -1L;
    }
    return ((Long)this.report.get(player.getUniqueId())).longValue() - System.currentTimeMillis();
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
  {
    if ((sender instanceof Player))
    {
      Player player = (Player)sender;
      if (arguments.length < 2)
      {
        player.sendMessage(new ColorHandler().translate("&cUsage: /" + label + " <playerName> <reason>"));
        return true;
      }
      Player target = Bukkit.getServer().getPlayerExact(arguments[0]);
      if(target.getName().equals(sender.getName()))
      {
    	  sender.sendMessage(new ColorHandler().translate("&cYou cannot report yourself!"));
    	  return true;
      }
      else if (isCooldownActive(player))
      {
        player.sendMessage(new ColorHandler().translate("&cYou can not report other player again for &6" + DurationFormatUtils.formatDurationWords(getMillisecondLeft(player), true, true) + "&c."));
      }
      else
      {
        setCooldown(player, TimeUtils.parse("1m"));
        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
          if (staff.hasPermission("lyra.player.staff"))
          {
        	MessageClick messageClick = new MessageClick("");
            messageClick.then(new ColorHandler().translate("&7[&9&lREPORT&7] &9" + player.getName() + " &ehas reported &9" + target.getName() + " &efor &c" + StringUtils.join(arguments, ' ', 1, arguments.length) + "&e."));
            messageClick.tooltip(new ColorHandler().translate("&aClick to teleport to " + target.getName()));
            messageClick.command("/teleport " + target.getName());
            messageClick.send(staff);
          }
        }
        player.sendMessage(new ColorHandler().translate("&eSucessfully reported the player &9" + target.getName() + " &efor &c" + StringUtils.join(arguments, ' ', 1, arguments.length) + "&e, report sent to all online staff members."));
      }
    }
    else
    {
      sender.sendMessage(new ColorHandler().translate("&cYou can not execute this command on console."));
    }
    return true;
  }
}
