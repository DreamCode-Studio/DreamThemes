package com.dreamcode.dreamthemes.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import com.dreamcode.dreamthemes.database.service.DatabaseService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
public abstract class DatabaseManager implements DatabaseService {
    protected HikariDataSource dataSource;

    public abstract void connect();

    public abstract void disconnect();

    public abstract CompletableFuture<String> getTheme(UUID uuid);

    public abstract CompletableFuture<Void> setTheme(UUID uuid, String theme);
}