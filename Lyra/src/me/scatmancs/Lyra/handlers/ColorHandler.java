package me.scatmancs.Lyra.handlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import org.bukkit.ChatColor;

public class ColorHandler {
    public String translate(String text) {
        return StringEscapeUtils.unescapeJava(ChatColor.translateAlternateColorCodes('&', text));
    }

    public List<String> translateFromArray(List<String> text) {
        List<String> messages = new ArrayList();

        for (String string : text) {
            messages.add(translate(string));
        }

        return messages;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
