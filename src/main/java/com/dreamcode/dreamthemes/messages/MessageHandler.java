package com.dreamcode.dreamthemes.messages;

import lombok.RequiredArgsConstructor;
import com.dreamcode.dreamthemes.configs.service.ConfigService;
import com.dreamcode.dreamthemes.messages.service.MessageService;
import com.dreamcode.dreamthemes.themes.service.ThemeService;
import com.dreamcode.dreamthemes.utils.HexColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class MessageHandler implements MessageService {
    private final ConfigService configService;
    private final ThemeService themeService;

    @Override
    public void sendMessage(CommandSender sender, String key) {
        String theme = sender instanceof Player ?
                themeService.getTheme(((Player) sender).getUniqueId()) : "CLASSIC";
        sendMessage(sender, key, theme);
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