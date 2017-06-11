package me.scatmancs.Lyra.listeners;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import me.scatmancs.Lyra.handlers.ColorHandler;
import me.scatmancs.Lyra.keys.AbstractKey;
import me.scatmancs.Lyra.keys.Key;
import me.scatmancs.Lyra.utils.Utilities;

public class KeyListener
  implements Listener
{
  private final Utilities utilities = Utilities.getInstance();
  private final Set<Player> openedCrate = new HashSet();
  
  private void decrementItemInHand(Player player)
  {
    ItemStack itemStack = player.getItemInHand();
    if (itemStack.getAmount() <= 1) {
      player.setItemInHand(new ItemStack(Material.AIR, 1));
    } else {
      itemStack.setAmount(itemStack.getAmount() - 1);
    }
  }
  
  private Inventory getKeyInventory(Player player, Key key)
  {
    int inventorySize = (key.getRolls() + 8) / 9 * 9;
    Inventory inventory = Bukkit.getServer().createInventory(player, inventorySize, new ColorHandler().translate("&9" + key.getName() + " Reward"));
    ItemStack[] loot = key.getLoot();
    if (loot != null)
    {
      Random random = new Random();
      for (int i = 0; i < key.getRolls(); i++)
      {
        ItemStack itemStack = loot[random.nextInt(loot.length)];
        if ((itemStack != null) && (itemStack.getType() != Material.AIR)) {
          inventory.setItem(i, itemStack);
        }
      }
    }
    else
    {
      player.sendMessage(new ColorHandler().translate("That key has no loot defined, please inform an admin."));
    }
    return inventory;
  }
  
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event)
  {
    AbstractKey abstractKey = this.utilities.getKeyManager().getByItemStack(event.getItemInHand());
    if (abstractKey != null) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    Player player = event.getPlayer();
    AbstractKey abstractKey = this.utilities.getKeyManager().getByItemStack(event.getItem());
    if (abstractKey != null)
    {
      Block block = event.getClickedBlock();
      if ((block != null) && (block.getType() == Material.ENDER_CHEST)) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            this.openedCrate.add(player);
            player.openInventory(getKeyInventory(player, (Key)abstractKey));
            decrementItemInHand(player);
            event.setCancelled(true);
        }
      }
    }
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event)
  {
    Player player = (Player)event.getPlayer();
    
    Inventory inventory = event.getInventory();
    Inventory topInventory = event.getView().getTopInventory();
    if ((inventory != null) && (topInventory != null) && (this.openedCrate.contains(player)))
    {
      boolean isEmpty = true;
      for (ItemStack itemStack : topInventory.getContents()) {
        if ((itemStack != null) && (itemStack.getType() != Material.AIR))
        {
          player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
          isEmpty = false;
        }
      }
      if (!isEmpty) {
        player.sendMessage(new ColorHandler().translate("&aYou closed your loot crate reward inventory, dropped on the ground."));
      }
      this.openedCrate.remove(player);
    }
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    Player player = event.getPlayer();
    if (this.openedCrate.contains(player)) {
      this.openedCrate.remove(player);
    }
  }
}
