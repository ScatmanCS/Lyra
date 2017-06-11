package me.scatmancs.Lyra.commands;

import com.google.common.primitives.Ints;

import me.scatmancs.Lyra.handlers.ColorHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class Command_setslots
  implements CommandExecutor, TabCompleter
{
  private void setMaxPlayers(int amount)
    throws ReflectiveOperationException
  {
    String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
    Object playerlist = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".CraftServer").getDeclaredMethod("getHandle", null).invoke(Bukkit.getServer(), null);
    Field maxplayers = playerlist.getClass().getSuperclass().getDeclaredField("maxPlayers");
    maxplayers.setAccessible(true);
    maxplayers.set(playerlist, Integer.valueOf(amount));
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
  {
    if (sender.hasPermission("lyra.command.setslots")) {
      if (arguments.length < 1)
      {
        sender.sendMessage(new ColorHandler().translate("&cUsage: /" + label + " <amount>"));
      }
      else
      {
        Integer amount = Ints.tryParse(arguments[0]);
        if (amount == null)
        {
          sender.sendMessage(new ColorHandler().translate("&c'" + amount + "' is not a valid number."));
        }
        else if (amount.intValue() <= 0)
        {
          sender.sendMessage(new ColorHandler().translate("&c'" + amount + "' is not a valid number."));
        }
        else
        {
          for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
            if (staff.isOp()) {
              staff.sendMessage(new ColorHandler().translate("&7[WARNING] &a" + sender.getName() + " &ehas updated the Slots capacity from " + Bukkit.getServer().getMaxPlayers() + " to " + amount + "."));
            }
          }
          try
          {
            setMaxPlayers(amount.intValue());
          }
          catch (ReflectiveOperationException expeption)
          {
            expeption.printStackTrace();
          }
        }
      }
    }
    return true;
  }
  
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments)
  {
    return Collections.emptyList();
  }
}
