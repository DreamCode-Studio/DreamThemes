package com.dreamcode.dreamthemes.configs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.dreamcode.dreamthemes.DreamThemes;
import com.dreamcode.dreamthemes.configs.service.ConfigService;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ConfigManager implements ConfigService {

    private FileConfiguration config;
    private DreamThemes plugin;

    public ConfigManager(DreamThemes plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    @Override
    public List<String> getThemes() {
        List<String> themes = new ArrayList<>(config.getStringList("themes"));
        if (!themes.contains("CLASSIC")) {
            themes.add("CLASSIC");
        }
        return themes;
    }

    @Override
    public String getDatabaseType() {
        return config.getString("database.type").toLowerCase();
    }

    @Override
    public String getMySQLHost() {
        return config.getString("database.mysql.host");
    }

    @Override
    public int getMySQLPort() {
        return config.getInt("database.mysql.port");
    }

    @Override
    public String getMySQLDatabase() {
        return config.getString("database.mysql.database");
    }

    @Override
    public String getMySQLUsername() {
        return config.getString("database.mysql.username");
    }

    @Override
    public String getMySQLPassword() {
        return config.getString("database.mysql.password");
    }

    @Override
    public long getCheckInterval() {
        return config.getLong("check-interval");
    }

    @Override
    public List<String> getMessages(String key, String theme) {
        return config.getStringList("messages." + key + "." + theme);
    }

    @Override
    public boolean reloadConfig() {
        try {
            plugin.reloadConfig();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public long getCacheDurationSeconds() {
        return config.getLong("cache.duration", 300);
    }

    @Override
    public long getCacheMaximumSize() {
        return config.getLong("cache.maximum-size", 1000);
    }
}