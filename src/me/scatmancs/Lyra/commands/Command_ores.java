package me.scatmancs.Lyra.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.scatmancs.Lyra.handlers.ColorHandler;

public class Command_ores
  implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
  {
    Player player = (Player) sender;
	if (args.length > 1)
    {
      sender.sendMessage(new ColorHandler().translate("&cUsage: /ores <player>"));
      return true;
    }
    if (args.length == 0)
    {
      sender.sendMessage(new ColorHandler().translate("&cUsage: /ores <player>"));
      return true;
    }
    Player target = Bukkit.getServer().getPlayer(args[0]);
    if ((args.length == 1) && 
      (target == null))
    {
      player.sendMessage("&Player not found");
      return true;
    }
    sender.sendMessage(new ColorHandler().translate("&7&m--------------------------------------------------"));
    sender.sendMessage(new ColorHandler().translate("                     &eOres mined by: &6" + target.getDisplayName()));
    sender.sendMessage(new ColorHandler().translate("&bDiamond Ore: &f" + target.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE)));
    sender.sendMessage(new ColorHandler().translate("&aEmerald Ore: &f" + target.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE)));
    sender.sendMessage(new ColorHandler().translate("&7Iron Ore: &f" + target.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE)));
    sender.sendMessage(new ColorHandler().translate("&6Gold Ore: &f" + target.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE)));
    sender.sendMessage(new ColorHandler().translate("&cRedstone Ore: &f" + target.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE)));
    sender.sendMessage(new ColorHandler().translate("&8Coal Ore: &f" + target.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE)));
    sender.sendMessage(new ColorHandler().translate("&9Lapis Ore: &f" + target.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE)));
    sender.sendMessage(new ColorHandler().translate("&7&m--------------------------------------------------"));
    return false;
  }
}
