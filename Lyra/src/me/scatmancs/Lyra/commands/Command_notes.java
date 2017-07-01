package me.scatmancs.Lyra.commands;

import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.scatmancs.Lyra.Lyra;
import me.scatmancs.Lyra.utils.UUIDFetcher;

public class Command_notes implements CommandExecutor {
    private File      notesFile;
    FileConfiguration data;
    ArrayList<String> notes;

    public Command_notes() {
        this.notesFile = new File(Lyra.getInstance().getDataFolder().getAbsolutePath(), "notes.yml");
        this.data      =
            YamlConfiguration.loadConfiguration(new File(Lyra.getInstance().getDataFolder().getAbsolutePath(),
                                                         "notes.yml"));
        this.notes = new ArrayList();
    }

    public boolean onCommand(CommandSender p, Command cmd, String label, String[] args) {
        if (!p.hasPermission("utilities.player.staff")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));

            return true;
        }

        if (args.length == 0) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Notes Help:"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &7/notes add [player] [note]"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &7/notes remove [player] [note number]"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &7/notes check [player]"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &7/notes clearall [player]"));

            return true;
        }

        if (args.length != 1) {
            if ((args.length == 2) && (args[0].equalsIgnoreCase("check"))) {
                String            player   = args[1];
                UUIDFetcher       fetcher  = new UUIDFetcher(Arrays.asList(new String[] { player }));
                Map<String, UUID> response = null;

                try {
                    response = fetcher.call();

                    if (!this.data.contains(response + ".Notes")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                             "&2" + args[1] + " &ccould not be found"));

                        return true;
                    }

                    if (this.data.getInt(response + ".Amount Of Notes") == 0) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                             "&2" + args[1]
                                                                             + " &cdoes not have any &6notes&c!"));

                        return true;
                    }

                    ArrayList list  = (ArrayList) this.data.getList(response + ".Notes");
                    String[]  notes = new String[63];

                    for (int i = 0; i < list.size(); i++) {
                        String tempVar = (String) list.get(i);

                        notes[i] = tempVar;
                    }

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2" + args[1] + "&f'&2s &6notes&7:"));

                    for (int i = 0; i < notes.length; i++) {
                        if (notes[i] == null) {
                            break;
                        }

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                             "&6[&e" + i + "&6] "
                                                                             + this.data.getString(
                                                                                 new StringBuilder(
                                                                                     String.valueOf(
                                                                                         String.valueOf(
                                                                                             p.getName()))).append(
                                                                                                 ".Last_Note_Date").toString()) + " &7" + notes[i]));
                    }
                } catch (Exception e) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cError!"));
                    e.printStackTrace();
                }
            }

            if (args[0].equalsIgnoreCase("clearall")) {
                String            player   = args[1];
                UUIDFetcher       fetcher  = new UUIDFetcher(Arrays.asList(new String[] { player }));
                Map<String, UUID> response = null;

                try {
                    response = fetcher.call();

                    if (!p.hasPermission("utilities.player.staff")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));

                        return true;
                    }

                    if (!this.data.contains(response + ".Notes")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                             "&2" + args[1] + " &ccould not be found"));

                        return true;
                    }

                    if (this.data.getInt(response + ".Amount Of Notes") == 0) {
                        p.sendMessage("§f" + args[1] + "§c has no notes.");

                        return true;
                    }

                    ArrayList clearList = (ArrayList) this.data.getList(response + ".Notes");

                    for (int j = 0; (j < clearList.size()) && (clearList.get(j) != null); j++) {
                        this.data.set(response + ".Amount Of Notes",
                                      Integer.valueOf(this.data.getInt(response + ".Amount Of Notes") - 1));
                        this.data.save(this.notesFile);
                        clearList.remove(j);
                        j--;
                    }

                    this.data.set(response + ".Notes", clearList);
                    this.data.save(this.notesFile);
                    p.sendMessage("§aCleared §f" + args[1] + "'s §anotes.");
                } catch (Exception e) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cError!"));
                    e.printStackTrace();
                }

                if (args[0].equalsIgnoreCase("add")) {
                    if (!p.hasPermission("utilities.player.staff")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));

                        return true;
                    }

                    if (!this.data.contains(response + ".Notes")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                             "&2" + args[1] + " &ccould not be found"));

                        return true;
                    }

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage&7: /note add [player] [note]"));

                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("remove")) {
                String            player   = args[1];
                UUIDFetcher       fetcher  = new UUIDFetcher(Arrays.asList(new String[] { player }));
                Map<String, UUID> response = null;

                try {
                    response = fetcher.call();

                    if (!p.hasPermission("utilities.player.staff")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));

                        return true;
                    }

                    if (!this.data.contains(response + ".Notes")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                             "&2" + args[1] + " &ccould not be found"));

                        return true;
                    }

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                         "&cUsage&7: /note remove [player] [note number]"));

                    return true;
                } catch (Exception e) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cError!"));
                    e.printStackTrace();
                }
            }

            if (args.length >= 3) {
                if (args[0].equalsIgnoreCase("clearall")) {
                    if (!p.hasPermission("utilities.player.staff")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));

                        return true;
                    }

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage&7: /note clearall [player]"));

                    return true;
                }

                if (args[0].equalsIgnoreCase("check")) {
                    String            player   = args[1];
                    UUIDFetcher       fetcher  = new UUIDFetcher(Arrays.asList(new String[] { player }));
                    Map<String, UUID> response = null;

                    try {
                        response = fetcher.call();

                        if (!this.data.contains(response + ".Notes")) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                                 "&2" + args[1]
                                                                                 + " &ccould not be found"));

                            return true;
                        }

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage&7: /note list [player]"));

                        return true;
                    } catch (Exception e) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cError!"));
                        e.printStackTrace();

                        if ((args[0].equalsIgnoreCase("remove")) && (!p.hasPermission("utilities.player.staff"))) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));

                            return true;
                        }

                        if (!this.data.contains(response + ".Notes")) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                                 "&2" + args[1]
                                                                                 + " &ccould not be found"));

                            return true;
                        }

                        if (this.data.getInt(response + ".Amount Of Notes") == 0) {
                            p.sendMessage("§f" + args[1] + "§c has no notes.");

                            return true;
                        }

                        try {
                            int       noteNumber = Integer.parseInt(args[2]);
                            ArrayList notes2     = (ArrayList) this.data.getList(response + ".Notes");

                            notes2.remove(noteNumber);
                            p.sendMessage("§aSuccessfully removed note §f#" + noteNumber + "§a.");
                            this.data.set(response + ".Notes", notes2);
                            this.data.set(response + ".Amount Of Notes",
                                          Integer.valueOf(this.data.getInt(response + ".Amount Of Notes") - 1));
                            this.data.save(this.notesFile);

                            return true;
                        } catch (Exception e1) {
                            p.sendMessage("§cInvalid note number.");

                            return true;
                        }
                    }
                }

                if (args[0].equalsIgnoreCase("add")) {
                    String            player   = args[1];
                    UUIDFetcher       fetcher  = new UUIDFetcher(Arrays.asList(new String[] { player }));
                    Map<String, UUID> response = null;

                    try {
                        response = fetcher.call();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }

                    if (!p.hasPermission("utilities.player.staff")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));

                        return true;
                    }

                    if (!this.data.contains(response + ".Notes")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                             "&2" + args[1] + " &ccould not be found"));

                        return true;
                    }

                    StringBuilder sb = new StringBuilder();

                    for (int j = 2; j < args.length; j++) {
                        sb.append(args[j]);
                        sb.append(" ");
                    }

                    Date             now    = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

                    this.data.set(response + ".Last_Note_Date", "[" + format.format(now) + "]");

                    String message = sb.toString();

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                         "&eAdded a &6note&e to &2" + args[1] + "&e."));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                         "&eNote&7: " + message.replaceAll("&", "§")));
                    this.notes.add(message);

                    if (this.data.getInt(response + ".Amount Of Notes") != 0) {
                        ArrayList list2 = (ArrayList) this.data.getList(response + ".Notes");

                        list2.add(String.valueOf(message.replaceAll("&", "§")) + "§r- by §e" + p.getName());
                        this.data.set(response + ".Notes", list2);
                        this.data.set(response + ".Amount Of Notes",
                                      Integer.valueOf(this.data.getInt(response + ".Amount Of Notes") + 1));

                        try {
                            this.data.save(this.notesFile);
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }

                        return true;
                    }

                    ArrayList list3 = new ArrayList();

                    list3.add(String.valueOf(message.replaceAll("&", "§")) + "§r- by §e" + p.getName());
                    this.data.set(response + ".Notes", list3);
                    this.data.set(response + ".Amount Of Notes",
                                  Integer.valueOf(this.data.getInt(response + ".Amount Of Notes") + 1));

                    try {
                        this.data.save(this.notesFile);
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }

                    return true;
                }
            }

            return false;
        }

        if (args[0].equalsIgnoreCase("clearall")) {
            if (!p.hasPermission("utilities.player.staff")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));

                return true;
            }

            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage&7: /note clearall [player]"));

            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (!p.hasPermission("utilities.player.staff")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));

                return true;
            }

            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage&7: /note add [player] [note]"));

            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (!p.hasPermission("utilities.player.staff")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));

                return true;
            }

            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                                 "&cUsage&7: /note remove [player] [note number]"));

            return true;
        }

        if (args[0].equalsIgnoreCase("check")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage&7: /note check [player]"));

            return true;
        }

        return true;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
