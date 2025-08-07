package com.dreamcode.dreamthemes.messages;

import com.dreamcode.dreamthemes.DreamThemes;
import lombok.RequiredArgsConstructor;
import com.dreamcode.dreamthemes.configs.service.ConfigService;
import com.dreamcode.dreamthemes.messages.service.MessageService;
import com.dreamcode.dreamthemes.themes.service.ThemeService;
import com.dreamcode.dreamthemes.utils.HexColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

@RequiredArgsConstructor
public class MessageHandler implements MessageService {
    private final ConfigService configService;
    private final ThemeService themeService;
    private final DreamThemes plugin;

    @Override
    public void sendMessage(CommandSender sender, String key) {
        if (!(sender instanceof Player)) {
            sendMessage(sender, key, "CLASSIC");
            return;
        }
        Player player = (Player) sender;
        themeService.getTheme(player.getUniqueId()).thenAccept(theme -> {
            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.runTask(plugin, () -> sendMessage(sender, key, theme));
        });
    }

    @Override
    public void sendMessage(CommandSender sender, String key, String theme) {
        List<String> messages = configService.getMessages(key, theme);
        if (messages != null && !messages.isEmpty()) {
            for (String msg : messages) {
                sender.sendMessage(HexColor.colorize(msg));
            }
        }
    }
}