package me.scatmancs.Lyra.keys.type;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import me.scatmancs.Lyra.keys.Key;

public class TierOneKey
  extends Key
{
  public TierOneKey()
  {
    super("TierOne", 3);
    
    ItemStack lootingSword = new ItemStack(Material.DIAMOND_SWORD, 1);
    lootingSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
    lootingSword.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
    lootingSword.addEnchantment(Enchantment.DURABILITY, 3);
    
    ItemStack fireSword = new ItemStack(Material.DIAMOND_SWORD, 1);
    fireSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
    fireSword.addEnchantment(Enchantment.FIRE_ASPECT, 1);
    
    ItemStack diamondHelmet = new ItemStack(Material.DIAMOND_HELMET, 1);
    diamondHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
    diamondHelmet.addEnchantment(Enchantment.DURABILITY, 3);
    
    ItemStack diamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
    diamondChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
    diamondChestplate.addEnchantment(Enchantment.DURABILITY, 3);
    
    ItemStack diamondLeggins = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
    diamondLeggins.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
    diamondLeggins.addEnchantment(Enchantment.DURABILITY, 3);
    
    ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS, 1);
    diamondBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
    diamondBoots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
    diamondBoots.addEnchantment(Enchantment.DURABILITY, 3);
    
    ItemStack diamondPickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
    diamondPickaxe.addEnchantment(Enchantment.DIG_SPEED, 5);
    diamondPickaxe.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
    diamondPickaxe.addEnchantment(Enchantment.DURABILITY, 3);
    
    ItemStack bow = new ItemStack(Material.BOW, 1);
    bow.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
    bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
    bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
    bow.addEnchantment(Enchantment.DURABILITY, 3);
    
    addReward(new ItemStack(Material.EMERALD_BLOCK, 8), 10);
    addReward(new ItemStack(Material.DIAMOND_BLOCK, 8), 10);
    addReward(new ItemStack(Material.GOLD_BLOCK, 8), 15);
    addReward(new ItemStack(Material.IRON_BLOCK, 8), 15);
    addReward(lootingSword, 2);
    addReward(fireSword, 4);
    addReward(bow, 2);
    addReward(diamondPickaxe, 2);
    addReward(diamondHelmet, 5);
    addReward(diamondChestplate, 5);
    addReward(diamondLeggins, 5);
    addReward(diamondBoots, 5);
    addReward(new ItemStack(Material.SULPHUR, 16), 3);
    addReward(new ItemStack(Material.GLOWSTONE, 8), 5);
    addReward(new ItemStack(Material.GOLDEN_APPLE, 16), 5);
    addReward(new ItemStack(Material.GHAST_TEAR, 16), 3);
    addReward(new ItemStack(Material.SKULL, 1, (short)1), 2);
    addReward(new ItemStack(Material.GOLDEN_APPLE, 1, (short)1), 2);
  }
  
  public ChatColor getColour()
  {
    return ChatColor.RED;
  }
}
