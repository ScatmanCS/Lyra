package me.scatmancs.Lyra;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.scatmancs.Lyra.commands.Command_list;
import me.scatmancs.Lyra.commands.Command_notes;
import me.scatmancs.Lyra.commands.Command_ores;
import me.scatmancs.Lyra.commands.Command_ping;
import me.scatmancs.Lyra.commands.Command_report;
import me.scatmancs.Lyra.handlers.ColorHandler;
import sun.rmi.transport.proxy.CGIHandler;


public class Lyra
  extends JavaPlugin
  implements Listener
{
  private static Lyra instance;

  public static Lyra getInstance()
  {
    if (instance == null) {
      instance = new Lyra();
    }
    return instance;
  }
  
  private String getDonorsOnline()
  {
    String message = "";
    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      if (player.hasPermission("lyra.player.toprank")) {
        message = message + ChatColor.GREEN + player.getName() + ChatColor.YELLOW + ", " + ChatColor.GREEN;
      }
    }
    if (message.length() > 2) {
      message = message.substring(0, message.length() - 2);
    }
    if (message.length() == 0) {
      message = ChatColor.GREEN + "None";
    }
    return message;
  }
  
  public void onEnable()
  {
    setup();
    
    getConsoleSender().sendMessage(new ColorHandler().translate("&7---------------------- Lyra ----------------------"));
    getConsoleSender().sendMessage(new ColorHandler().translate("&7* Version: " + getDescription().getVersion() + "."));
    getConsoleSender().sendMessage(new ColorHandler().translate("&7* Plugin Author(s): &e" + getDevelopers() + "&7."));
    getConsoleSender().sendMessage(new ColorHandler().translate("&7* Plugin Status: Activated."));
    getConsoleSender().sendMessage(new ColorHandler().translate("&7-------------------------------------------------------"));
    
    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
    {
      public void run()
      {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Online Top Ranks: " + ChatColor.GREEN + Lyra.this.getDonorsOnline());
      }
    }, 0L, 6000L);
  }
  
  public void onDisable()
  {
    instance = null;
    
    getConsoleSender().sendMessage(new ColorHandler().translate("&7---------------------- Lyra ----------------------"));
    getConsoleSender().sendMessage(new ColorHandler().translate("&7* Version: " + getDescription().getVersion() + "."));
    getConsoleSender().sendMessage(new ColorHandler().translate("&7* Plugin Author(s): &e" + getDevelopers() + "&7."));
    getConsoleSender().sendMessage(new ColorHandler().translate("&7* Plugin Status: Deactivated."));
    getConsoleSender().sendMessage(new ColorHandler().translate("&7-------------------------------------------------------"));
  }
  
  private void setup()
  {
    instance = this;
    
    setupFiles();
         
    Bukkit.getServer().getPluginCommand("list").setExecutor(new Command_list());
    Bukkit.getServer().getPluginCommand("notes").setExecutor(new Command_notes());
    Bukkit.getServer().getPluginCommand("ores").setExecutor(new Command_ores());
    Bukkit.getServer().getPluginCommand("ping").setExecutor(new Command_ping());
    Bukkit.getServer().getPluginCommand("report").setExecutor(new Command_report());
    
    long timeMillis = System.currentTimeMillis();
    getConsoleSender().sendMessage(new ColorHandler().translate("&9[Lyra] Plugin loaded in " + (System.currentTimeMillis() - timeMillis) + "ms."));
  }
  
  public ConsoleCommandSender getConsoleSender()
  {
    return Bukkit.getServer().getConsoleSender();
  }
  
  public List<String> getDevelopers()
  {
    return Arrays.asList(new String[] { "Wart" });
  }
  
  public FileConfigurationOptions getFileConfigurationOptions()
  {
    return getConfig().options();
  }
  
  public String getString(String path)
  {
    if (getConfig().contains(path)) {
      return new ColorHandler().translate(getConfig().getString(path));
    }
    return new ColorHandler().translate("&cString not found: '" + path + "'");
  }
  
  public List<String> getStringList(String path)
  {
    if (getConfig().contains(path))
    {
      ArrayList<String> lines = new ArrayList();
      for (String text : getConfig().getStringList(path)) {
        lines.add(new ColorHandler().translate(text));
      }
      return lines;
    }
    return Arrays.asList(new String[] { new ColorHandler().translate("&cString list not found: '" + path + "'") });
  }
  
  private void setupFiles()
  {
    try
    {
      if (!getDataFolder().exists()) {
        getDataFolder().mkdirs();
      }
      File file = new File(getDataFolder().getAbsolutePath(), "config.yml");
      File file2 = new File(getDataFolder().getAbsolutePath(), "pin.yml");
      File file3 = new File(getDataFolder().getAbsolutePath(), "fails.yml");
      File file4 = new File(getDataFolder().getAbsoluteFile(), "notes.yml");
      if (!file.exists())
      {
        getFileConfigurationOptions().copyDefaults(true);
        saveConfig();
        getConsoleSender().sendMessage(new ColorHandler().translate("&6[Lyra] The config file was not detected, because of the file does not exist it will created."));
      }
      else
      {
        getConsoleSender().sendMessage(new ColorHandler().translate("&a[Lyra] Successfully loaded all configuration files."));
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
  }
  
  public void createPinFile()
  {
    try
    {
      FileConfiguration pin = YamlConfiguration.loadConfiguration(new File(getInstance().getDataFolder(), "pin.yml"));
      File pinFile = new File(instance.getDataFolder(), "pin.yml");
      pin.save(pinFile);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public void createFailedFile()
  {
    try
    {
      FileConfiguration fail = YamlConfiguration.loadConfiguration(new File(getInstance().getDataFolder(), "fails.yml"));
      File failFile = new File(instance.getDataFolder(), "fails.yml");
      fail.save(failFile);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
