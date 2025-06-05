package com.dreamcode.dreamthemes.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexColor {

    public static String colorize(String message) {
        Pattern pattern = Pattern.compile("&#([0-9a-fA-F]{6})");
        Matcher matcher = pattern.matcher(message);
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            result.append(message, lastEnd, matcher.start());
            String hex = matcher.group(1);
            result.append(ChatColor.of("#" + hex).toString());
            lastEnd = matcher.end();
        }
        result.append(message.substring(lastEnd));
        return ChatColor.translateAlternateColorCodes('&', result.toString());
    }
}