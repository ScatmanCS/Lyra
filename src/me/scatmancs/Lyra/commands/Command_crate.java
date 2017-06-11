package me.scatmancs.Lyra.commands;

import com.google.common.primitives.Ints;

import me.scatmancs.Lyra.handlers.ColorHandler;
import me.scatmancs.Lyra.keys.AbstractKey;
import me.scatmancs.Lyra.keys.Key;
import me.scatmancs.Lyra.utils.Utilities;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Command_crate
  implements CommandExecutor
{
  private final Utilities utilities = Utilities.getInstance();
  
  private String getKeys()
  {
    String message = "";
    for (Key keys : this.utilities.getKeyManager().getKeys()) {
      message = message + keys.getName() + ", ";
    }
    if (message.length() > 2) {
      message = message.substring(0, message.length() - 2);
    }
    if (message.length() == 0) {
      message = "None";
    }
    return message;
  }
  
  public boolean isInventoryEmpty(Player player)
  {
    PlayerInventory playerInventory = player.getPlayer().getInventory();
    for (int size = 0; size < 35; size++) {
      if (playerInventory.getItem(size) != null) {
        return false;
      }
    }
    return true;
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
  {
    if (sender.hasPermission("lyra.command.crate"))
    {
      if (arguments.length == 0)
      {
        sender.sendMessage(new ColorHandler().translate("&7&m-----------------------------------------------------"));
        sender.sendMessage(new ColorHandler().translate(" &5&lCrate Commands"));
        sender.sendMessage(new ColorHandler().translate("  &d/crate list &7- &eSee a list of all keys."));
        sender.sendMessage(new ColorHandler().translate("  &d/crate give <playerName> <keyName> <amount> &7- &eGive related key to a player."));
        sender.sendMessage(new ColorHandler().translate("&7&m-----------------------------------------------------"));
        return false;
      }
      if (arguments.length >= 1) {
        switch (arguments[0].toLowerCase())
        {
        case "list": 
          sender.sendMessage(new ColorHandler().translate("&eList of Available Crate Keys: &7" + getKeys()));
          break;
        case "give": 
          if (arguments.length < 3)
          {
            sender.sendMessage(new ColorHandler().translate("&cUsage: /" + label + " <playerName> <keyName> <amount>"));
            return true;
          }
          Player target = Bukkit.getServer().getPlayer(arguments[1]);
          if (target == null)
          {
            sender.sendMessage(new ColorHandler().translate("&cPlayer called '" + arguments[1] + "' not found."));
            return true;
          }
          AbstractKey abstractKey = this.utilities.getKeyManager().getByName(arguments[2]);
          if (abstractKey == null)
          {
            sender.sendMessage(new ColorHandler().translate("&cCrate Key called '" + arguments[2] + "' not found."));
            return true;
          }
          Integer amount = null;
          if (arguments.length >= 4)
          {
            Integer amount1 = Ints.tryParse(arguments[3]);
            if (amount1 == null)
            {
              sender.sendMessage(new ColorHandler().translate("'" + arguments[3] + "' is not a number."));
              return true;
            }
          }
          else
          {
            amount = Integer.valueOf(1);
          }
          if (amount.intValue() <= 0)
          {
            sender.sendMessage(new ColorHandler().translate("&cYou can only give keys in positive quantities."));
            return true;
          }
          ItemStack itemStack = abstractKey.getItemStack().clone();
          if (amount.intValue() > 64)
          {
            sender.sendMessage(new ColorHandler().translate("&cYou can not give Crate Keys in quantities for more than 64."));
            return true;
          }
          itemStack.setAmount(amount.intValue());
          Map<Integer, ItemStack> excess = target.getInventory().addItem(new ItemStack[] { itemStack });
          for (ItemStack entry : excess.values()) {
            target.getLocation().getWorld().dropItemNaturally(target.getLocation(), entry);
          }
          sender.sendMessage(new ColorHandler().translate("&eSuccessfully given x" + amount + " &r" + abstractKey.getDisplayName() + " &ekey to &a" + target.getName() + "&e."));
          break;
        default: 
          sender.sendMessage(new ColorHandler().translate("&cCrate argument called '" + arguments[0] + "' not found."));
        }
      }
    }
    else
    {
      sender.sendMessage(new ColorHandler().translate("&cYou do not have permissions to execute this command."));
    }
    return true;
  }
}
