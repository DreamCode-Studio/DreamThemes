package com.dreamcode.dreamthemes.api;

import java.util.UUID;

public interface ThemeAPI {
    String getTheme(UUID playerUUID);

    String getTheme(String playerName);

    void setTheme(UUID playerUUID, String theme);

    void setTheme(String playerName, String theme);
}