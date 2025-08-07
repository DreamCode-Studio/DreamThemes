package com.dreamcode.dreamthemes.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ThemeAPI {
    String getTheme(UUID playerUUID);

    String getTheme(String playerName);

    void setTheme(UUID playerUUID, String theme);

    void setTheme(String playerName, String theme);

    CompletableFuture<String> getThemeAsync(UUID playerUUID);

    CompletableFuture<String> getThemeAsync(String playerName);

    CompletableFuture<Void> setThemeAsync(UUID playerUUID, String theme);

    CompletableFuture<Void> setThemeAsync(String playerName, String theme);
}