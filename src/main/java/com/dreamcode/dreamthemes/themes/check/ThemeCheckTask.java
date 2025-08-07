package com.dreamcode.dreamthemes.themes.check;

import lombok.RequiredArgsConstructor;
import com.dreamcode.dreamthemes.DreamThemes;
import com.dreamcode.dreamthemes.configs.service.ConfigService;
import com.dreamcode.dreamthemes.themes.service.ThemeService;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@RequiredArgsConstructor
public class ThemeCheckTask {
    private final DreamThemes plugin;
    private final ThemeService themeService;
    private final ConfigService configService;

    public void start() {
        long interval = configService.getCheckInterval() * 20L;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    themeService.getTheme(uuid).thenAccept(theme -> {
                        if (!theme.equalsIgnoreCase("CLASSIC") &&
                                !player.hasPermission("dreamthemes." + theme.toLowerCase())) {
                            themeService.setTheme(uuid, "CLASSIC");
                        }
                    });
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, interval);
    }
}