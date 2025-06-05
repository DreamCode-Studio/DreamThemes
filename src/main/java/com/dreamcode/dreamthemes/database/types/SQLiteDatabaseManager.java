package com.dreamcode.dreamthemes.database.types;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import com.dreamcode.dreamthemes.DreamThemes;
import com.dreamcode.dreamthemes.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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
    @SneakyThrows
    @Synchronized
    public String getTheme(UUID uuid) {
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
    }

    @Override
    @SneakyThrows
    @Synchronized
    public void setTheme(UUID uuid, String theme) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT OR REPLACE INTO player_themes (uuid, currentStyle) VALUES (?, ?)")) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, theme);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}