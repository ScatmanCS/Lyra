package me.scatmancs.Lyra.keys.type;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.scatmancs.Lyra.keys.Key;


public class ObtainKey
  extends Key
{
  public ObtainKey()
  {
    super("Obtain", 3);
    
    addReward(new ItemStack(Material.SPECKLED_MELON, 10), 10);
    addReward(new ItemStack(Material.SULPHUR, 6), 10);
    addReward(new ItemStack(Material.BLAZE_ROD, 4), 7);
    addReward(new ItemStack(Material.SUGAR, 6), 10);
    addReward(new ItemStack(Material.SPIDER_EYE, 4), 10);
    addReward(new ItemStack(Material.GLOWSTONE_DUST, 8), 10);
    addReward(new ItemStack(Material.GLASS_BOTTLE, 16), 15);
    addReward(new ItemStack(Material.ENDER_PEARL, 1), 5);
    addReward(new ItemStack(Material.POTATO_ITEM, 4), 3);
    addReward(new ItemStack(Material.COOKED_BEEF, 8), 16);
    addReward(new ItemStack(Material.GOLDEN_APPLE, 1, (short)1), 1);
  }
  
  public ChatColor getColour()
  {
    return ChatColor.BLUE;
  }
}
