package com.dreamcode.dreamthemes.database.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface DatabaseService {
    void connect();

    void disconnect();

    CompletableFuture<String> getTheme(UUID uuid);

    CompletableFuture<Void> setTheme(UUID uuid, String theme);
}