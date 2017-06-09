package me.scatmancs.Lyra;

import org.bukkit.plugin.java.JavaPlugin;

import me.scatmancs.Lyra.commands.Command_ores;
import me.scatmancs.Lyra.listeners.ElevatorListener;

public class Lyra extends JavaPlugin {

	public void onEnable()
	{
		getLogger().info("Lyra has been enabled!");
		registerListeners();
		registerCommands();
	}
	
	public void onDisable()
	{
		getLogger().info("Lyra has been enabled!");
	}
	
	public void registerListeners()
	{
		getServer().getPluginManager().registerEvents(new ElevatorListener(), this);
	}
	
	public void registerCommands()
	{
		getCommand("ores").setExecutor(new Command_ores());
	}
}
