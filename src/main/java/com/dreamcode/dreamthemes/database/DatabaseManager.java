package com.dreamcode.dreamthemes.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import com.dreamcode.dreamthemes.database.service.DatabaseService;

import java.util.UUID;

@Getter
@Setter
public abstract class DatabaseManager implements DatabaseService {
    protected HikariDataSource dataSource;

    public abstract void connect();

    public abstract void disconnect();

    public abstract String getTheme(UUID uuid);

    public abstract void setTheme(UUID uuid, String theme);
}