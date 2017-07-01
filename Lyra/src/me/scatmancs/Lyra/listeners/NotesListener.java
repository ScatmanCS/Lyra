package me.scatmancs.Lyra.listeners;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.scatmancs.Lyra.Lyra;
import me.scatmancs.Lyra.utils.UUIDFetcher;
import me.scatmancs.Lyra.utils.Utilities;

public class NotesListener implements Listener {
    private File      notesFile;
    FileConfiguration data;

    public NotesListener() {
        this.notesFile = new File(Lyra.getInstance().getDataFolder().getAbsoluteFile(), "notes.yml");
        this.data      =
            YamlConfiguration.loadConfiguration(new File(Lyra.getInstance().getDataFolder().getAbsoluteFile(),
                                                         "notes.yml"));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws Exception {
        Player            p        = e.getPlayer();
        String            player   = p.getName();
        UUIDFetcher       fetcher  = new UUIDFetcher(Arrays.asList(new String[] { player }));
        Map<String, UUID> response = null;

        response = fetcher.call();

        if ((this.data.contains(response + ".Notes")) && (this.data.getInt(response + ".Amount Of Notes") != 0)) {
            ArrayList list  = (ArrayList) this.data.getList(response + ".Notes");
            String[]  notes = new String[63];
            String    tempVar;

            for (int i = 0; i < list.size(); i++) {
                tempVar  = (String) list.get(i);
                notes[i] = tempVar;
            }

            for (int i = 0; i < notes.length; i++) {
                if (notes[i] == null) {
                    break;
                }

                for (Player staff : Bukkit.getOnlinePlayers()) {
                    if (staff.hasPermission("utilities.staff")) {
                        staff.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                                 "&2" + p.getName()
                                                                                 + " &f'&es notes&7:"));
                        staff.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                                 "&6[&e" + i + "&6] "
                                                                                 + this.data.getString(
                                                                                     new StringBuilder(
                                                                                         String.valueOf(
                                                                                             String.valueOf(
                                                                                                 p.getName()))).append(
                                                                                                     ".Last_Note_Date").toString()) + " &7" + notes[i]));
                    }
                }
            }
        }

        if (!this.data.contains(response + ".Notes")) {
            this.data.set(response + ".Notes", Integer.valueOf(0));
            this.data.set(response + ".Amount Of Notes", Integer.valueOf(0));
            this.data.save(this.notesFile);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
