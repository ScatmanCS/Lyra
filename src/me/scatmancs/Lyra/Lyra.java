package me.scatmancs.Lyra;

import org.bukkit.plugin.java.JavaPlugin;

import me.scatmancs.Lyra.commands.Command_list;
import me.scatmancs.Lyra.commands.Command_ores;
import me.scatmancs.Lyra.commands.Command_ping;
import me.scatmancs.Lyra.commands.Command_report;
import me.scatmancs.Lyra.fixes.HitDelayFix;
import me.scatmancs.Lyra.listeners.DonatorJoinListener;
import me.scatmancs.Lyra.listeners.ElevatorListener;
import me.scatmancs.Lyra.listeners.WeatherListener;

public class Lyra extends JavaPlugin {

	public void onEnable()
	{
		getLogger().info("Lyra has been enabled!");
		registerListeners();
		registerCommands();
		// loadConfig();
	}
	
	public void onDisable()
	{
		getLogger().info("Lyra has been enabled!");
	}
	
	public void registerListeners()
	{
		getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
		getServer().getPluginManager().registerEvents(new DonatorJoinListener(), this);
		getServer().getPluginManager().registerEvents(new WeatherListener(), this);
		getServer().getPluginManager().registerEvents(new HitDelayFix(), this);
	}
	
	public void loadConfig()
	{
		getConfig().options().copyDefaults();
		saveConfig();
	}
	
	public void registerCommands()
	{
		getCommand("ores").setExecutor(new Command_ores());
		getCommand("ping").setExecutor(new Command_ping());
		getCommand("report").setExecutor(new Command_report());
		getCommand("list").setExecutor(new Command_list());
	}
}
