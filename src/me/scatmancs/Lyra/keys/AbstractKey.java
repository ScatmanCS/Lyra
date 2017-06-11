package me.scatmancs.Lyra.keys;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractKey
{
  private final String name;
  
  public String getName()
  {
    return this.name;
  }
  
  public AbstractKey(String name)
  {
    this.name = name;
  }
  
  public String getDisplayName()
  {
    return getColour() + this.name;
  }
  
  public abstract ChatColor getColour();
  
  public abstract ItemStack getItemStack();
}
