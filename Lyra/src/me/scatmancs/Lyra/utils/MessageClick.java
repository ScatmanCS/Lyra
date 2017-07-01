package me.scatmancs.Lyra.utils;

import java.io.StringWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Achievement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.Statistic.Type;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MessageClick {
    private Class<?>                nmsChatSerializer    = Reflection.getNMSClass("ChatSerializer");
    private Class<?>                nmsTagCompound       = Reflection.getNMSClass("NBTTagCompound");
    private Class<?>                nmsPacketPlayOutChat = Reflection.getNMSClass("PacketPlayOutChat");
    private Class<?>                nmsAchievement       = Reflection.getNMSClass("Achievement");
    private Class<?>                nmsStatistic         = Reflection.getNMSClass("Statistic");
    private Class<?>                nmsItemStack         = Reflection.getNMSClass("ItemStack");
    private Class<?>                obcStatistic         = Reflection.getOBCClass("CraftStatistic");
    private Class<?>                obcItemStack         = Reflection.getOBCClass("inventory.CraftItemStack");
    private final List<MessagePart> messageParts;
    private String                  jsonString;
    private boolean                 dirty;

    public MessageClick(String firstPartText) {
        this.messageParts = new ArrayList();
        this.messageParts.add(new MessagePart(firstPartText));
        this.jsonString = null;
        this.dirty      = false;
    }

    public MessageClick achievementTooltip(Achievement which) {
        try {
            Object achievement = Reflection.getMethod(this.obcStatistic, "getNMSAchievement", new Class[0])
                                           .invoke(null, new Object[] { which });

            return achievementTooltip((String) Reflection.getField(this.nmsAchievement, "name").get(achievement));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public MessageClick achievementTooltip(String name) {
        onHover("show_achievement", "achievement." + name);

        return this;
    }

    public MessageClick color(ChatColor color) {
        if (!color.isColor()) {
            throw new IllegalArgumentException(color.name() + " is not a color");
        }

        latest().color = color;
        this.dirty     = true;

        return this;
    }

    public MessageClick command(String command) {
        onClick("run_command", command);

        return this;
    }

    public MessageClick file(String path) {
        onClick("open_file", path);

        return this;
    }

    public MessageClick itemTooltip(ItemStack itemStack) {
        try {
            Object nmsItem = Reflection.getMethod(this.obcItemStack, "asNMSCopy", new Class[] { ItemStack.class })
                                       .invoke(null, new Object[] { itemStack });

            return itemTooltip(Reflection.getMethod(this.nmsItemStack, "save", new Class[0])
                                         .invoke(nmsItem, new Object[] { this.nmsTagCompound.newInstance() })
                                         .toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public MessageClick itemTooltip(String itemJSON) {
        onHover("show_item", itemJSON);

        return this;
    }

    private MessagePart latest() {
        return (MessagePart) this.messageParts.get(this.messageParts.size() - 1);
    }

    public MessageClick link(String url) {
        onClick("open_url", url);

        return this;
    }

    private String makeMultilineTooltip(String[] lines) {
        StringWriter string = new StringWriter();
        JsonWriter   json   = new JsonWriter(string);

        try {
            json.beginObject().name("id").value(1L);
            json.name("tag").beginObject().name("display").beginObject();
            json.name("Name").value("\\u00A7f" + lines[0].replace("\"", "\\\""));
            json.name("Lore").beginArray();

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];

                json.value(line.isEmpty()
                           ? " "
                           : line.replace("\"", "\\\""));
            }

            json.endArray().endObject().endObject().endObject();
            json.close();
        } catch (Exception e) {
            throw new RuntimeException("invalid tooltip");
        }

        return string.toString();
    }

    public void onClick(String name, String data) {
        MessagePart latest = latest();

        latest.clickActionName = name;
        latest.clickActionData = data;
        this.dirty             = true;
    }

    private void onHover(String name, String data) {
        MessagePart latest = latest();

        latest.hoverActionName = name;
        latest.hoverActionData = data;
        this.dirty             = true;
    }

    public void send(Iterable<Player> players) {
        for (Player player : players) {
            send(player);
        }
    }

    public void send(Player player) {
        try {
            Object handle     = Reflection.getHandle(player);
            Object connection = Reflection.getField(handle.getClass(), "playerConnection").get(handle);
            Object serialized = Reflection.getMethod(this.nmsChatSerializer, "a", new Class[] { String.class })
                                          .invoke(null, new Object[] { toJSONString() });
            Object packet = this.nmsPacketPlayOutChat.getConstructor(new Class[] {
                                Reflection.getNMSClass("IChatBaseComponent") })
                                                     .newInstance(new Object[] { serialized });

            Reflection.getMethod(connection.getClass(), "sendPacket", new Class[0])
                      .invoke(connection, new Object[] { packet });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MessageClick statisticTooltip(Statistic which) {
        Statistic.Type type = which.getType();

        if (type != Statistic.Type.UNTYPED) {
            throw new IllegalArgumentException("That statistic requires an additional " + type + " parameter!");
        }

        try {
            Object statistic = Reflection.getMethod(this.obcStatistic, "getNMSStatistic", new Class[0])
                                         .invoke(null, new Object[] { which });

            return achievementTooltip((String) Reflection.getField(this.nmsStatistic, "name").get(statistic));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public MessageClick statisticTooltip(Statistic which, EntityType entity) {
        Statistic.Type type = which.getType();

        if (type == Statistic.Type.UNTYPED) {
            throw new IllegalArgumentException("That statistic needs no additional parameter!");
        }

        if (type != Statistic.Type.ENTITY) {
            throw new IllegalArgumentException("Wrong parameter type for that statistic - needs " + type + "!");
        }

        try {
            Object statistic = Reflection.getMethod(this.obcStatistic, "getEntityStatistic", new Class[0])
                                         .invoke(null, new Object[] { which, entity });

            return achievementTooltip((String) Reflection.getField(this.nmsStatistic, "name").get(statistic));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public MessageClick statisticTooltip(Statistic which, Material item) {
        Statistic.Type type = which.getType();

        if (type == Statistic.Type.UNTYPED) {
            throw new IllegalArgumentException("That statistic needs no additional parameter!");
        }

        if (((type == Statistic.Type.BLOCK) && (item.isBlock())) || (type == Statistic.Type.ENTITY)) {
            throw new IllegalArgumentException("Wrong parameter type for that statistic - needs " + type + "!");
        }

        try {
            Object statistic = Reflection.getMethod(this.obcStatistic, "getMaterialStatistic", new Class[0])
                                         .invoke(null, new Object[] { which, item });

            return achievementTooltip((String) Reflection.getField(this.nmsStatistic, "name").get(statistic));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public MessageClick style(ChatColor... styles) {
        ChatColor[] arrayOfChatColor;
        int         j = (arrayOfChatColor = styles).length;

        for (int i = 0; i < j; i++) {
            ChatColor style = arrayOfChatColor[i];

            if (!style.isFormat()) {
                throw new IllegalArgumentException(style.name() + " is not a style");
            }
        }

        latest().styles = styles;
        this.dirty      = true;

        return this;
    }

    public MessageClick suggest(String command) {
        onClick("suggest_command", command);

        return this;
    }

    public MessageClick then(Object obj) {
        this.messageParts.add(new MessagePart(obj.toString()));
        this.dirty = true;

        return this;
    }

    public String toJSONString() {
        if ((!this.dirty) && (this.jsonString != null)) {
            return this.jsonString;
        }

        StringWriter string = new StringWriter();
        JsonWriter   json   = new JsonWriter(string);

        try {
            if (this.messageParts.size() == 1) {
                latest().writeJson(json);
            } else {
                json.beginObject().name("text").value("").name("extra").beginArray();

                for (MessagePart messagePart : this.messageParts) {
                    messagePart.writeJson(json);
                }

                json.endArray().endObject();
                json.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("invalid message");
        }

        this.jsonString = string.toString();
        this.dirty      = false;

        return this.jsonString;
    }

    public MessageClick tooltip(List<String> lines) {
        return tooltip((String[]) lines.toArray());
    }

    public MessageClick tooltip(String text) {
        return tooltip(text.split("\\n"));
    }

    public MessageClick tooltip(String... lines) {
        if (lines.length == 1) {
            onHover("show_text", lines[0]);
        } else {
            itemTooltip(makeMultilineTooltip(lines));
        }

        return this;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
