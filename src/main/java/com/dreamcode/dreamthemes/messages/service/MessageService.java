package com.dreamcode.dreamthemes.messages.service;

import org.bukkit.command.CommandSender;

public interface MessageService {
    void sendMessage(CommandSender sender, String key);

    void sendMessage(CommandSender sender, String key, String theme);
}