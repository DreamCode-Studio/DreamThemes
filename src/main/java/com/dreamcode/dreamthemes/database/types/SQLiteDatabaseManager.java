package com.dreamcode.dreamthemes.database.types;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import com.dreamcode.dreamthemes.DreamThemes;
import com.dreamcode.dreamthemes.database.DatabaseManager;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class SQLiteDatabaseManager extends DatabaseManager {
    private final DreamThemes plugin;

    @Override
    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + plugin.getDataFolder() + "/themes.db");
        config.setDriverClassName("org.sqlite.JDBC");
        dataSource = new HikariDataSource(config);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS player_themes (uuid TEXT PRIMARY KEY, currentStyle TEXT NOT NULL)")) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Override
    public CompletableFuture<String> getTheme(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT currentStyle FROM player_themes WHERE uuid = ?")) {
                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("currentStyle");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "CLASSIC";
        }, runnable -> Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
    }

    @Override
    public CompletableFuture<Void> setTheme(UUID uuid, String theme) {
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT OR REPLACE INTO player_themes (uuid, currentStyle) VALUES (?, ?)")) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, theme);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, runnable -> Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
    }
}