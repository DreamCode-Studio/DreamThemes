package com.dreamcode.dreamthemes.api.impl;

import lombok.RequiredArgsConstructor;
import com.dreamcode.dreamthemes.api.ThemeAPI;
import com.dreamcode.dreamthemes.themes.service.ThemeService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class ThemeAPIImpl implements ThemeAPI {
    private final ThemeService themeService;

    @Override
    public String getTheme(UUID playerUUID) {
        return themeService.getTheme(playerUUID).join();
    }

    @Override
    public String getTheme(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return "CLASSIC";
        }
        return themeService.getTheme(player.getUniqueId()).join();
    }

    @Override
    public void setTheme(UUID playerUUID, String theme) {
        themeService.setTheme(playerUUID, theme).join();
    }

    @Override
    public void setTheme(String playerName, String theme) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            themeService.setTheme(player.getUniqueId(), theme).join();
        }
    }

    @Override
    public CompletableFuture<String> getThemeAsync(UUID playerUUID) {
        return themeService.getTheme(playerUUID);
    }

    @Override
    public CompletableFuture<String> getThemeAsync(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return CompletableFuture.completedFuture("CLASSIC");
        }
        return themeService.getTheme(player.getUniqueId());
    }

    @Override
    public CompletableFuture<Void> setThemeAsync(UUID playerUUID, String theme) {
        return themeService.setTheme(playerUUID, theme);
    }

    @Override
    public CompletableFuture<Void> setThemeAsync(String playerName, String theme) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return themeService.setTheme(player.getUniqueId(), theme);
        }
        return CompletableFuture.completedFuture(null);
    }
}