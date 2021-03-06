package me.scatmancs.Lyra.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.scatmancs.Lyra.Lyra;


public class NotesConfig
{
    public FileConfiguration config     = null;
    public File              configFile = null;
    final Lyra         plugin;

    public NotesConfig(Lyra instance)
    {
        this.plugin = instance;
    }

    public void loadConfig()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void reloadConfig()
    {
        if(this.configFile == null)
        {
            this.configFile = new File(this.plugin.getDataFolder(), "notes.yml");
        }

        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void saveConfig()
    {
        if((this.config == null) || (this.configFile == null))
        {
            return;
        }

        try
        {
            this.config.save(this.configFile);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig()
    {
        if(this.config == null)
        {
            reloadConfig();
        }

        return this.config;
    }
}
