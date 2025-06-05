package com.dreamcode.dreamthemes.database.service;

import java.util.UUID;

public interface DatabaseService {
    void connect();

    void disconnect();

    String getTheme(UUID uuid);

    void setTheme(UUID uuid, String theme);
}