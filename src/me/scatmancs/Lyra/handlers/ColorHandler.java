package me.scatmancs.Lyra.handlers;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;

public class ColorHandler
{
	  public String translate(String text)
	  {
	    return StringEscapeUtils.unescapeJava(ChatColor.translateAlternateColorCodes('&', text));
	  }
}
