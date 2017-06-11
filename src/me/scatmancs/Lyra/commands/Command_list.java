package me.scatmancs.Lyra.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.scatmancs.Lyra.handlers.ColorHandler;
import me.scatmancs.Lyra.utils.Utilities;

public class Command_list
  implements CommandExecutor, TabCompleter
{
  private final Utilities utilities = Utilities.getInstance();
  
  private String getStaffOnline()
  {
    String message = "";
    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      if ((!this.utilities.getStaffModeListener().isStaffModeActive(player)) && (!this.utilities.getStaffModeListener().isVanished(player)) && (player.hasPermission("lyra.player.staff"))) {
        message = message + player.getName() + ", ";
      }
    }
    if (message.length() > 2) {
      message = message.substring(0, message.length() - 2);
    }
    if (message.length() == 0) {
      message = "None";
    }
    return message;
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
  {
    if (arguments.length >= 0)
    {
      sender.sendMessage(new ColorHandler().translate("&6&m-----------------------------------------------------"));
      sender.sendMessage(new ColorHandler().translate("&ePlayers Online&7: &a" + Bukkit.getServer().getOnlinePlayers().length + "&7/&a" + Bukkit.getServer().getMaxPlayers()));
      sender.sendMessage(new ColorHandler().translate("&eCurrent Staff Members&7:"));
      sender.sendMessage(new ColorHandler().translate(" &a" + getStaffOnline()));
      sender.sendMessage(new ColorHandler().translate(" &6* &7If you need assistance join to our TeamSpeak (ts.lyra.com)"));
      sender.sendMessage(new ColorHandler().translate("&6&m-----------------------------------------------------"));
    }
    return true;
  }
  
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments)
  {
    return Collections.emptyList();
  }
}
