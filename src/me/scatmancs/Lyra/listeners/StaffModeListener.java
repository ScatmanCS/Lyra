package me.scatmancs.Lyra.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.scatmancs.Lyra.handlers.ColorHandler;
import me.scatmancs.Lyra.utils.TimeUtils;
import me.scatmancs.Lyra.utils.Utilities;

public class StaffModeListener
  implements Listener
{
  private final Utilities utilities = Utilities.getInstance();
  private final Set<UUID> staffMode = new HashSet();
  private final Set<UUID> staffChat = new HashSet();
  private final Set<UUID> vanished = new HashSet();
  private final HashMap<UUID, UUID> inspectedPlayer = new HashMap();
  private final HashMap<UUID, ItemStack[]> contents = new HashMap();
  private final HashMap<UUID, ItemStack[]> armorContents = new HashMap();
  public Inventory inv;
  
  public Player getInspectedPlayer(Player player)
  {
    return Bukkit.getServer().getPlayer((UUID)this.inspectedPlayer.get(player.getUniqueId()));
  }
  
  public boolean isVanished(Player player)
  {
    return this.vanished.contains(player.getUniqueId());
  }
  
  public boolean isStaffChatActive(Player player)
  {
    return this.staffChat.contains(player.getUniqueId());
  }
  
  public boolean isStaffModeActive(Player player)
  {
    return this.staffMode.contains(player.getUniqueId());
  }
  
  public boolean hasPreviousInventory(Player player)
  {
    return (this.contents.containsKey(player.getUniqueId())) && (this.armorContents.containsKey(player.getUniqueId()));
  }
  
  public void saveInventory(Player player)
  {
    this.contents.put(player.getUniqueId(), player.getInventory().getContents());
    this.armorContents.put(player.getUniqueId(), player.getInventory().getArmorContents());
  }
  
  public void loadInventory(Player player)
  {
    PlayerInventory playerInventory = player.getInventory();
    playerInventory.setContents((ItemStack[])this.contents.get(player.getUniqueId()));
    playerInventory.setArmorContents((ItemStack[])this.armorContents.get(player.getUniqueId()));
    this.contents.remove(player.getUniqueId());
    this.armorContents.remove(player.getUniqueId());
  }
  
  public void setStaffChat(Player player, boolean status)
  {
    if (status)
    {
      if (player.hasPermission("lyra.player.staff")) {
        this.staffChat.add(player.getUniqueId());
      }
    }
    else {
      this.staffChat.remove(player.getUniqueId());
    }
  }
  
  public void setStaffMode(Player player, boolean status)
  {
    if (status)
    {
      if (player.hasPermission("lyra.player.staff"))
      {
        this.staffMode.add(player.getUniqueId());
        saveInventory(player);
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setArmorContents(new ItemStack[] { new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
        playerInventory.clear();
        setItems(player);
        setVanished(player, true);
        player.updateInventory();
        if (player.getGameMode() == GameMode.SURVIVAL) {
          player.setGameMode(GameMode.CREATIVE);
        }
      }
      else
      {
        player.sendMessage(new ColorHandler().translate("&cYou do not have permissions to enable the staffmode."));
      }
    }
    else
    {
      this.staffMode.remove(player.getUniqueId());
      PlayerInventory playerInventory = player.getInventory();
      playerInventory.setArmorContents(new ItemStack[] { new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
      playerInventory.clear();
      setVanished(player, false);
      if (hasPreviousInventory(player)) {
        loadInventory(player);
      }
      player.updateInventory();
      if ((!player.hasPermission("essentials.creative")) && (player.getGameMode() == GameMode.CREATIVE)) {
        player.setGameMode(GameMode.SURVIVAL);
      }
    }
  }
  
  public void setVanished(Player player, boolean status)
  {
    Object playerInventory;
    if (status)
    {
      this.vanished.add(player.getUniqueId());
      for (Player online : Bukkit.getServer().getOnlinePlayers()) {
        if (!online.hasPermission("lyra.player.staff")) {
          online.hidePlayer(player);
        }
      }
      if (isStaffModeActive(player))
      {
        playerInventory = player.getInventory();
        ((PlayerInventory)playerInventory).setItem(7, getVanishItemFor(player));
      }
    }
    else
    {
      this.vanished.remove(player.getUniqueId());
      for (playerInventory = Bukkit.getServer().getOnlinePlayers().length; ((Iterator)playerInventory).hasNext();)
      {
        Player online = (Player)((Iterator)playerInventory).next();
        
        online.showPlayer(player);
      }
      if (isStaffModeActive(player))
      {
        PlayerInventory playerInventory1 = player.getInventory();
        playerInventory1.setItem(7, getVanishItemFor(player));
      }
    }
  }
  
  public void setItems(Player player)
  {
    PlayerInventory playerInventory = player.getInventory();
    
    ItemStack compass = new ItemStack(Material.COMPASS, 1);
    ItemMeta compassMeta = compass.getItemMeta();
    compassMeta.setDisplayName(new ColorHandler().translate("&bTeleport Compass"));
    compassMeta.setLore(new ColorHandler().translateFromArray(Arrays.asList(new String[] { "&7Right click block: Move through", "&7Left click: Move to block in line of sight" })));
    compass.setItemMeta(compassMeta);
    
    ItemStack book = new ItemStack(Material.BOOK, 1);
    ItemMeta bookMeta = book.getItemMeta();
    bookMeta.setDisplayName(new ColorHandler().translate("&bInspection Tool"));
    bookMeta.setLore(new ColorHandler().translateFromArray(Arrays.asList(new String[] { "&7Right click player to inspect inventory" })));
    book.setItemMeta(bookMeta);
    
    ItemStack blazeRod = new ItemStack(Material.ICE, 1);
    ItemMeta blazeRodMeta = blazeRod.getItemMeta();
    blazeRodMeta.setDisplayName(new ColorHandler().translate("&bFreeze Player"));
    blazeRodMeta.setLore(new ColorHandler().translateFromArray(Arrays.asList(new String[] { "&7Right click player to update freeze status" })));
    blazeRod.setItemMeta(blazeRodMeta);
    
    ItemStack carpet = new ItemStack(Material.WOOD_AXE);
    ItemMeta carpetMeta = carpet.getItemMeta();
    carpetMeta.setDisplayName(new ColorHandler().translate("&bWorld Edit"));
    carpetMeta.setLore(new ColorHandler().translateFromArray(Arrays.asList(new String[] { "&7The normal worldedit wand" })));
    carpet.setItemMeta(carpetMeta);
    
    ItemStack xrayGui = new ItemStack(Material.DIAMOND_PICKAXE);
    ItemMeta xrayGuiMeta = xrayGui.getItemMeta();
    xrayGuiMeta.setDisplayName(new ColorHandler().translate("&bXrayer Gui"));
    xrayGuiMeta.setLore(new ColorHandler().translateFromArray(Arrays.asList(new String[] { "&7Used to get a list of possible xrayers" })));
    xrayGui.setItemMeta(xrayGuiMeta);
    
    ItemStack record10 = new ItemStack(Material.RECORD_10, 1);
    ItemMeta record10Meta = record10.getItemMeta();
    record10Meta.setDisplayName(new ColorHandler().translate("&bRandom Teleportation"));
    record10Meta.setLore(new ColorHandler().translateFromArray(Arrays.asList(new String[] { "&7Right click to teleport to a random player" })));
    record10.setItemMeta(record10Meta);
    
    playerInventory.setItem(0, compass);
    playerInventory.setItem(1, book);
    playerInventory.setItem(4, blazeRod);
    playerInventory.setItem(2, carpet);
    playerInventory.setItem(7, getVanishItemFor(player));
    playerInventory.setItem(8, record10);
    playerInventory.setItem(6, xrayGui);
  }
  
  private ItemStack getVanishItemFor(Player player)
  {
    ItemStack inkSack = null;
    if (isVanished(player))
    {
      inkSack = new ItemStack(Material.INK_SACK, 1, (short)8);
      ItemMeta inkSackMeta = inkSack.getItemMeta();
      inkSackMeta.setDisplayName(new ColorHandler().translate("&bVanished: &aTrue"));
      inkSackMeta.setLore(new ColorHandler().translateFromArray(Arrays.asList(new String[] { "&7Right click to update your vanish status." })));
      inkSack.setItemMeta(inkSackMeta);
    }
    else
    {
      inkSack = new ItemStack(Material.INK_SACK, 1, (short)10);
      ItemMeta inkSackMeta = inkSack.getItemMeta();
      inkSackMeta.setDisplayName(new ColorHandler().translate("&bVanished: &cFalse"));
      inkSackMeta.setLore(new ColorHandler().translateFromArray(Arrays.asList(new String[] { "&7Right click to update your vanish status." })));
      inkSack.setItemMeta(inkSackMeta);
    }
    return inkSack;
  }
  
  public void openInspectionMenu(final Player player, final Player target)
  {
    final Inventory inventory = Bukkit.getServer().createInventory(null, 54, new ColorHandler().translate("&eInspecting: &c" + target.getName()));
    
    new BukkitRunnable()
    {
      public void run()
      {
        StaffModeListener.this.inspectedPlayer.put(player.getUniqueId(), target.getUniqueId());
        
        PlayerInventory playerInventory = target.getInventory();
        
        ItemStack speckledMelon = new ItemStack(Material.SPECKLED_MELON);
        ItemMeta speckledMelonMeta = speckledMelon.getItemMeta();
        speckledMelonMeta.setDisplayName(new ColorHandler().translate("&aHealth"));
        speckledMelon.setItemMeta(speckledMelonMeta);
        
        ItemStack cookedBeef = new ItemStack(Material.COOKED_BEEF, target.getFoodLevel());
        ItemMeta cookedBeefMeta = cookedBeef.getItemMeta();
        cookedBeefMeta.setDisplayName(new ColorHandler().translate("&aHunger"));
        cookedBeef.setItemMeta(cookedBeefMeta);
        
        ItemStack brewingStand = new ItemStack(Material.BREWING_STAND_ITEM, target.getPlayer().getActivePotionEffects().size());
        ItemMeta brewingStandMeta = brewingStand.getItemMeta();
        brewingStandMeta.setDisplayName(new ColorHandler().translate("&aActive Potion Effects"));
        ArrayList<String> brewingStandLore = new ArrayList();
        for (PotionEffect potionEffect : target.getPlayer().getActivePotionEffects())
        {
          String effectName = potionEffect.getType().getName();
          int effectLevel = potionEffect.getAmplifier();
          effectLevel++;
          brewingStandLore.add(new ColorHandler().translate("&e" + WordUtils.capitalizeFully(effectName).replace("_", " ") + " " + effectLevel + "&7: &c" + TimeUtils.IntegerCountdown.setFormat(Integer.valueOf(potionEffect.getDuration() / 20))));
        }
        brewingStandMeta.setLore(brewingStandLore);
        brewingStand.setItemMeta(brewingStandMeta);
        
        ItemStack compass = new ItemStack(Material.COMPASS, 1);
        ItemMeta compassMeta = compass.getItemMeta();
        compassMeta.setDisplayName(new ColorHandler().translate("&aPlayer Location"));
        compassMeta.setLore(new ColorHandler().translateFromArray(Arrays.asList(new String[] { "&eWorld&7: &a" + player.getWorld().getName(), "&eCoords", "  &eX&7: &c" + target.getLocation().getBlockX(), "  &eY&7: &c" + target.getLocation().getBlockY(), "  &eZ&7: &c" + target.getLocation().getBlockZ() })));
        compass.setItemMeta(compassMeta);
        
        ItemStack ice = new ItemStack(Material.ICE, 1);
        ItemMeta iceMeta = ice.getItemMeta();
        iceMeta.setLore(new ColorHandler().translateFromArray(Arrays.asList(new String[] { "&eClick to update freeze status" })));
        ice.setItemMeta(iceMeta);
        
        ItemStack orangeGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)1);
        
        inventory.setContents(playerInventory.getContents());
        inventory.setItem(36, playerInventory.getHelmet());
        inventory.setItem(37, playerInventory.getChestplate());
        inventory.setItem(38, playerInventory.getLeggings());
        inventory.setItem(39, playerInventory.getBoots());
        inventory.setItem(40, playerInventory.getItemInHand());
        for (int i = 41; i <= 46; i++) {
          inventory.setItem(i, orangeGlassPane);
        }
        inventory.setItem(47, speckledMelon);
        inventory.setItem(48, cookedBeef);
        inventory.setItem(49, brewingStand);
        inventory.setItem(50, compass);
        inventory.setItem(51, ice);
        for (int i = 52; i <= 53; i++) {
          inventory.setItem(i, orangeGlassPane);
        }
        if (!player.getOpenInventory().getTitle().equals(new ColorHandler().translate("&eInspecting: &c" + target.getName())))
        {
          cancel();
          StaffModeListener.this.inspectedPlayer.remove(player.getUniqueId());
        }
      }
    }
    
      .runTaskTimer((Plugin) this.utilities, 0L, 5L);
    
    player.openInventory(inventory);
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event)
  {
    Player player = (Player)event.getWhoClicked();
    ItemStack clicked = event.getCurrentItem();
    Inventory inventory = event.getInventory();
    if (event.getClickedInventory() != null)
    {
      if (isStaffModeActive(player)) {
        event.setCancelled(true);
      }
      if ((inventory.getName().equals("Xrayer Gui")) && 
        (clicked.getType() == Material.SKULL_ITEM)) {
        Bukkit.dispatchCommand(player, "tp " + clicked.getItemMeta().getDisplayName());
      }
      if (event.getInventory().getTitle().contains(new ColorHandler().translate("&eInspecting: ")))
      {
        Player inspected = getInspectedPlayer(player);
        if (event.getRawSlot() == 51) {
          if (inspected != null) {

          }
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    Action action = event.getAction();
    if ((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK))
    {
      Player player = event.getPlayer();
      if ((isStaffModeActive(player)) && (player.hasPermission("lyra.player.staff")))
      {
        ItemStack itemStack = player.getItemInHand();
        if (itemStack != null)
        {
          if (itemStack.getType() == Material.DIAMOND_PICKAXE)
          {
            int Counter = 0;
            this.inv = Bukkit.createInventory(null, 54, "Xrayer Gui");
            for (Player players : Bukkit.getOnlinePlayers())
            {
              Counter++;
              if (Counter < 54)
              {
                if (players.getLocation().getBlockY() < 20)
                {
                  ItemStack xSkull = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
                  ItemMeta xSkullMeta = xSkull.getItemMeta();
                  xSkullMeta.setDisplayName(players.getName());
                  xSkullMeta.setLore(new ColorHandler().translateFromArray(Arrays.asList(new String[] { "&eThis player is at level &6&l" + players.getLocation().getBlockY() })));
                  xSkull.setItemMeta(xSkullMeta);
                  this.inv.addItem(new ItemStack[] { xSkull });
                }
              }
              else {
                event.getPlayer().sendMessage(new ColorHandler().translate("&cThere are too many players mining right now."));
              }
            }
            player.openInventory(this.inv);
          }
          else if (itemStack.getType() == Material.INK_SACK) {
            if (isVanished(player)) {
              setVanished(player, false);
            } else {
              setVanished(player, true);
            }
          }
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
  {
    if ((event.getRightClicked() != null) && ((event.getRightClicked() instanceof Player)))
    {
      Player player = event.getPlayer();
      if ((isStaffModeActive(player)) && (player.hasPermission("lyra.player.staff")))
      {
        ItemStack itemStack = player.getItemInHand();
        if (itemStack != null)
        {
          Player target = (Player)event.getRightClicked();
          if (itemStack.getType() == Material.BOOK)
          {
            if ((target != null) && (!player.getName().equals(target.getName())))
            {
              openInspectionMenu(player, target);
              player.sendMessage(new ColorHandler().translate("&eYou are now inspecting the inventory of &c" + target.getName() + "&e."));
            }
          }
          else if (itemStack.getType() == Material.CARROT_STICK)
          {
            if ((target != null) && (!player.getName().equals(target.getName())))
            {
              if (player.getVehicle() != null) {
                player.getVehicle().eject();
              }
              target.setPassenger(player);
            }
          }
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    if ((!player.hasPermission("essentials.gamemode.creative")) && (player.getGameMode() == GameMode.CREATIVE)) {
      player.setGameMode(GameMode.CREATIVE);
    }
    if (player.hasPermission("lyra.player.staff"))
    {
      Bukkit.getServer().getConsoleSender().sendMessage(new ColorHandler().translate("&aStaff Connected: &f" + player.getName() + "."));
      for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
        if (staff.hasPermission("lyra.player.staff")) {
          staff.sendMessage(new ColorHandler().translate("&aStaff Connected: &f" + player.getName() + "."));
        }
      }
    }
    else if (this.vanished.size() > 0)
    {
      for (UUID uuid : this.vanished)
      {
        Player vanishedPlayer = Bukkit.getServer().getPlayer(uuid);
        if (vanishedPlayer != null) {
          player.hidePlayer(vanishedPlayer);
        }
      }
    }
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    Player player = event.getPlayer();
    if (player.hasPermission("lyra.player.staff"))
    {
      Bukkit.getServer().getConsoleSender().sendMessage(new ColorHandler().translate("&cStaff Disconnected: &f" + player.getName() + "."));
      for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
        if (staff.hasPermission("lyra.player.staff")) {
          staff.sendMessage(new ColorHandler().translate("&cStaff Disconnected: &f" + player.getName() + "."));
        }
      }
    }
    if (isStaffModeActive(player))
    {
      this.staffMode.remove(player.getUniqueId());
      this.inspectedPlayer.remove(player.getUniqueId());
      PlayerInventory playerInventory = player.getInventory();
      playerInventory.setArmorContents(new ItemStack[] { new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
      playerInventory.clear();
      setVanished(player, false);
      if (hasPreviousInventory(player)) {
        loadInventory(player);
      }
      if ((!player.hasPermission("essentials.gamemode.creative")) && (player.getGameMode() == GameMode.CREATIVE)) {
        player.setGameMode(GameMode.SURVIVAL);
      }
    }
  }
  
  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent event)
  {
    Player player = event.getPlayer();
    if (isStaffModeActive(player)) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onPlayerPickupItem(PlayerPickupItemEvent event)
  {
    Player player = event.getPlayer();
    if ((isVanished(player)) || (isStaffModeActive(player))) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event)
  {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    if ((isVanished(player)) || (isStaffModeActive(player))) {
      if (block != null) {
        event.setCancelled(true);
      }
    }
  }
  
  @EventHandler
  public void onBlockBreak(BlockBreakEvent event)
  {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    if ((isVanished(player)) || (isStaffModeActive(player))) {
      if (block != null) {
        event.setCancelled(true);
      }
    }
  }
  
  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
  {
    if (((event.getEntity() instanceof Player)) && ((event.getDamager() instanceof Player)))
    {
      Player player = (Player)event.getDamager();
      if ((isVanished(player)) || ((isStaffModeActive(player)) && (player.hasPermission("lyra.player.staff")))) {
        event.setCancelled(true);
      }
    }
    else if (((event.getEntity() instanceof LivingEntity)) && ((event.getDamager() instanceof Player)))
    {
      Player player = (Player)event.getDamager();
      if ((isVanished(player)) || ((isStaffModeActive(player)) && (player.hasPermission("lyra.player.staff")))) {
        event.setCancelled(true);
      }
    }
  }
}
