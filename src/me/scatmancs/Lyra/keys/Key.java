package me.scatmancs.Lyra.keys;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.scatmancs.Lyra.handlers.ColorHandler;

public class Key
  extends AbstractKey
{
  private final ItemStack[] items;
  private final int rolls;
  
  public int getRolls()
  {
    return this.rolls;
  }
  
  public Key(String keyName, int rolls)
  {
    super(keyName);
    
    this.items = new ItemStack[100];
    this.rolls = rolls;
  }
  
  public ItemStack[] getLoot()
  {
    return (ItemStack[])Arrays.copyOf(this.items, this.items.length);
  }
  
  public void addReward(ItemStack itemStack, int percent)
  {
    int currentItems = 0;
    for (ItemStack items : this.items) {
      if ((items != null) && (items.getType() != Material.AIR)) {
        currentItems++;
      }
    }
    int min = Math.min(100, currentItems + percent);
    for (int i = currentItems; i < min; i++) {
      this.items[i] = itemStack;
    }
  }
  
  public ChatColor getColour()
  {
    return ChatColor.WHITE;
  }
  
  public ItemStack getItemStack()
  {
    ItemStack tripwireHook = new ItemStack(Material.TRIPWIRE_HOOK, 1);
    ItemMeta tripwireHookMeta = tripwireHook.getItemMeta();
    tripwireHookMeta.setDisplayName(getDisplayName() + " Key");
    tripwireHook.setItemMeta(tripwireHookMeta);
    return tripwireHook;
  }
}
