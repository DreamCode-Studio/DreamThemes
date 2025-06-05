package com.dreamcode.dreamthemes.themes;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.dreamcode.dreamthemes.configs.service.ConfigService;
import com.dreamcode.dreamthemes.database.service.DatabaseService;
import com.dreamcode.dreamthemes.themes.service.ThemeService;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class ThemeManager implements ThemeService {
    private final DatabaseService databaseService;
    private final ConfigService configService;
    @Getter
    private final LoadingCache<UUID, String> themeCache;

    public ThemeManager(DatabaseService databaseService, ConfigService configService) {
        this.databaseService = databaseService;
        this.configService = configService;
        this.themeCache = CacheBuilder.newBuilder()
                .expireAfterWrite(configService.getCacheDurationSeconds(), TimeUnit.SECONDS)
                .maximumSize(configService.getCacheMaximumSize())
                .build(new CacheLoader<UUID, String>() {
                    @Override
                    public String load(@NotNull UUID uuid) {
                        return databaseService.getTheme(uuid);
                    }
                });
    }

    @Override
    public String getTheme(UUID uuid) {
        return themeCache.getUnchecked(uuid);
    }

    @Override
    public void setTheme(UUID uuid, String themeInput) {
        String theme = findTheme(themeInput);
        if (theme == null && !themeInput.equalsIgnoreCase("CLASSIC")) {
            return;
        }
        theme = theme != null ? theme : "CLASSIC";
        databaseService.setTheme(uuid, theme);
        themeCache.put(uuid, theme);
    }

    @Override
    public String findTheme(String input) {
        for (String theme : configService.getThemes()) {
            if (theme.equalsIgnoreCase(input)) {
                return theme;
            }
        }
        return null;
    }

    @Override
    public boolean hasThemePermission(Player player, String theme) {
        return player.hasPermission("dreamthemes." + theme.toLowerCase());
    }

    @Override
    public void clearCache(UUID uuid) {
        themeCache.invalidate(uuid);
    }

    @Override
    public List<String> getAvailableThemes() {
        return configService.getThemes();
    }
}