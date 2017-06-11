package me.scatmancs.Lyra.commands;

import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.scatmancs.Lyra.handlers.ColorHandler;
import me.scatmancs.Lyra.utils.Utilities;

public class Command_staffmode
  implements CommandExecutor, TabCompleter
{
  private final Utilities utilities = Utilities.getInstance();
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
  {
    if ((sender instanceof Player))
    {
      Player player = (Player)sender;
      if (player.hasPermission("lyra.command.staffmode"))
      {
        if (arguments.length > 0)
        {
          player.sendMessage(new ColorHandler().translate("&cUsage: /" + label));
        }
        else if (this.utilities.getStaffModeListener().isStaffModeActive(player))
        {
          this.utilities.getStaffModeListener().setStaffMode(player, false);
          player.sendMessage(new ColorHandler().translate("&eYour staff mode has been &cdisabled&e."));
        }
        else
        {
          this.utilities.getStaffModeListener().setStaffMode(player, true);
          player.sendMessage(new ColorHandler().translate("&eYour staff mode has been &aenabled&e."));
        }
      }
      else {
        player.sendMessage(new ColorHandler().translate("&cYou do not have permissions to execute this command."));
      }
    }
    else
    {
      sender.sendMessage(new ColorHandler().translate("&cYou can not execute this command on console."));
    }
    return true;
  }
  
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments)
  {
    if (arguments.length > 1) {
      return Collections.emptyList();
    }
    return null;
  }
}
