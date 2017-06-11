package me.scatmancs.Lyra.keys;

import com.google.common.collect.Sets;

import me.scatmancs.Lyra.keys.type.ObtainKey;
import me.scatmancs.Lyra.keys.type.TierOneKey;
import me.scatmancs.Lyra.keys.type.TierThreeKey;
import me.scatmancs.Lyra.keys.type.TierTwoKey;

import java.util.Set;
import org.bukkit.inventory.ItemStack;

public class KeyManager
{
  private final Set<Key> keys;
  private final ObtainKey obtainKey;
  private final TierOneKey tierOneKey;
  private final TierTwoKey tierTwoKey;
  private final TierThreeKey tierThreeKey;
  
  public Set<Key> getKeys()
  {
    return this.keys;
  }
  
  public ObtainKey getObtainKey()
  {
    return this.obtainKey;
  }
  
  public TierOneKey getTierOneKey()
  {
    return this.tierOneKey;
  }
  
  public TierTwoKey getTierTwoKey()
  {
    return this.tierTwoKey;
  }
  
  public TierThreeKey getTierThreeKey()
  {
    return this.tierThreeKey;
  }
  
  public KeyManager()
  {
    this.keys = Sets.newHashSet(new Key[] { this.obtainKey = new ObtainKey(), this.tierOneKey = new TierOneKey(), this.tierTwoKey = new TierTwoKey(), this.tierThreeKey = new TierThreeKey() });
  }
  
  public Key getByName(String name)
  {
    for (Key keys : this.keys) {
      if (keys.getName().equalsIgnoreCase(name)) {
        return keys;
      }
    }
    return null;
  }
  
  @Deprecated
  public Key getByClass(Class<? extends Key> clazz)
  {
    for (Key keys : this.keys) {
      if (clazz.isAssignableFrom(keys.getClass())) {
        return keys;
      }
    }
    return null;
  }
  
  public Key getByItemStack(ItemStack itemStack)
  {
    if ((itemStack == null) || (!itemStack.hasItemMeta())) {
      return null;
    }
    for (Key keys : this.keys)
    {
      ItemStack key = keys.getItemStack();
      if (key.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) {
        return keys;
      }
    }
    return null;
  }
}
