package me.scatmancs.Lyra;

import org.bukkit.plugin.java.JavaPlugin;

import me.scatmancs.Lyra.commands.Command_crate;
import me.scatmancs.Lyra.commands.Command_list;
import me.scatmancs.Lyra.commands.Command_ores;
import me.scatmancs.Lyra.commands.Command_ping;
import me.scatmancs.Lyra.commands.Command_report;
import me.scatmancs.Lyra.commands.Command_setslots;
import me.scatmancs.Lyra.commands.Command_staffmode;
import me.scatmancs.Lyra.commands.Command_vanish;
import me.scatmancs.Lyra.fixes.HitDelayFix;
import me.scatmancs.Lyra.listeners.DonatorJoinListener;
import me.scatmancs.Lyra.listeners.ElevatorListener;
import me.scatmancs.Lyra.listeners.KeyListener;
import me.scatmancs.Lyra.listeners.StaffModeListener;
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
		getServer().getPluginManager().registerEvents(new KeyListener(), this);
		getServer().getPluginManager().registerEvents(new WeatherListener(), this);
		getServer().getPluginManager().registerEvents(new StaffModeListener(), this);
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
		getCommand("setslots").setExecutor(new Command_setslots());
		getCommand("report").setExecutor(new Command_report());
		getCommand("crate").setExecutor(new Command_crate());
		getCommand("list").setExecutor(new Command_list());
		getCommand("staffmode").setExecutor(new Command_staffmode());
		getCommand("vanish").setExecutor(new Command_vanish());
	}
}
