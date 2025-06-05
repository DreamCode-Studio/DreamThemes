package com.dreamcode.dreamthemes.api.impl;

import lombok.RequiredArgsConstructor;
import com.dreamcode.dreamthemes.api.ThemeAPI;
import com.dreamcode.dreamthemes.themes.service.ThemeService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class ThemeAPIImpl implements ThemeAPI {
    private final ThemeService themeService;

    @Override
    public String getTheme(UUID playerUUID) {
        return themeService.getTheme(playerUUID);
    }

    @Override
    public String getTheme(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return "CLASSIC";
        }
        return themeService.getTheme(player.getUniqueId());
    }

    @Override
    public void setTheme(UUID playerUUID, String theme) {
        themeService.setTheme(playerUUID, theme);
    }

    @Override
    public void setTheme(String playerName, String theme) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            themeService.setTheme(player.getUniqueId(), theme);
        }
    }
}