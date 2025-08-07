package com.dreamcode.dreamthemes.database.types;

import com.dreamcode.dreamthemes.DreamThemes;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import com.dreamcode.dreamthemes.configs.service.ConfigService;
import com.dreamcode.dreamthemes.database.DatabaseManager;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class MySQLDatabaseManager extends DatabaseManager {
    private final ConfigService configService;
    private final DreamThemes plugin = DreamThemes.getInstance();

    @Override
    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + configService.getMySQLHost() + ":" +
                configService.getMySQLPort() + "/" + configService.getMySQLDatabase());
        config.setUsername(configService.getMySQLUsername());
        config.setPassword(configService.getMySQLPassword());
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource = new HikariDataSource(config);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS player_themes (uuid VARCHAR(36) PRIMARY KEY, currentStyle VARCHAR(50) NOT NULL)")) {
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
                         "INSERT INTO player_themes (uuid, currentStyle) VALUES (?, ?) ON DUPLICATE KEY UPDATE currentStyle = ?")) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, theme);
                stmt.setString(3, theme);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, runnable -> Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
    }
}