package me.scatmancs.Lyra.keys.type;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import me.scatmancs.Lyra.keys.Key;

public class TierThreeKey
  extends Key
{
  public TierThreeKey()
  {
    super("TierThree", 3);
    
    ItemStack lootingSword = new ItemStack(Material.DIAMOND_SWORD, 1);
    lootingSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
    lootingSword.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 5);
    lootingSword.addEnchantment(Enchantment.DURABILITY, 3);
    
    ItemStack fireSowrd = new ItemStack(Material.DIAMOND_SWORD, 1);
    fireSowrd.addEnchantment(Enchantment.DAMAGE_ALL, 1);
    fireSowrd.addEnchantment(Enchantment.FIRE_ASPECT, 2);
    fireSowrd.addEnchantment(Enchantment.DURABILITY, 3);
    
    ItemStack diamondHelmet = new ItemStack(Material.DIAMOND_HELMET, 1);
    diamondHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
    diamondHelmet.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
    
    ItemStack diamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
    diamondChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
    diamondChestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
    
    ItemStack diamondLeggins = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
    diamondLeggins.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
    diamondLeggins.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
    
    ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS, 1);
    diamondBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
    diamondBoots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 5);
    diamondBoots.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
    
    ItemStack diamondPickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
    diamondPickaxe.addEnchantment(Enchantment.DIG_SPEED, 5);
    diamondPickaxe.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 5);
    diamondPickaxe.addEnchantment(Enchantment.DURABILITY, 3);
    
    ItemStack bow = new ItemStack(Material.BOW, 1);
    bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
    bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
    bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
    
    addReward(new ItemStack(Material.EMERALD_BLOCK, 32), 10);
    addReward(new ItemStack(Material.DIAMOND_BLOCK, 32), 10);
    addReward(new ItemStack(Material.GOLD_BLOCK, 32), 15);
    addReward(new ItemStack(Material.IRON_BLOCK, 32), 15);
    addReward(lootingSword, 2);
    addReward(fireSowrd, 4);
    addReward(bow, 2);
    addReward(diamondPickaxe, 2);
    addReward(diamondHelmet, 5);
    addReward(diamondChestplate, 5);
    addReward(diamondLeggins, 5);
    addReward(diamondBoots, 5);
    addReward(new ItemStack(Material.SULPHUR, 64), 3);
    addReward(new ItemStack(Material.GLOWSTONE, 64), 5);
    addReward(new ItemStack(Material.GOLDEN_APPLE, 32), 5);
    addReward(new ItemStack(Material.GHAST_TEAR, 64), 3);
    addReward(new ItemStack(Material.BEACON, 1), 2);
    addReward(new ItemStack(Material.GOLDEN_APPLE, 4, (short)1), 2);
  }
  
  public ChatColor getColour()
  {
    return ChatColor.RED;
  }
}
