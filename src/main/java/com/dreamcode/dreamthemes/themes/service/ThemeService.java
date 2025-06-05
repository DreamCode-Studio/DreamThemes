package com.dreamcode.dreamthemes.themes.service;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface ThemeService {
    String getTheme(UUID uuid);

    void setTheme(UUID uuid, String theme);

    String findTheme(String input);

    boolean hasThemePermission(Player player, String theme);

    void clearCache(UUID uuid);

    List<String> getAvailableThemes();
}