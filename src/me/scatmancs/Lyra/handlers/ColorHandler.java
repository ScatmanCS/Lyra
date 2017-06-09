package me.scatmancs.Lyra.handlers;

import org.bukkit.ChatColor;

public class ColorHandler
{
  public static String translate(String input)
  {
    return ChatColor.translateAlternateColorCodes('&', input);
  }
}
