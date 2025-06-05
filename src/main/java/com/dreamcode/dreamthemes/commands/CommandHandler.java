package com.dreamcode.dreamthemes.commands;

import lombok.RequiredArgsConstructor;
import com.dreamcode.dreamthemes.DreamThemes;
import com.dreamcode.dreamthemes.configs.service.ConfigService;
import com.dreamcode.dreamthemes.messages.service.MessageService;
import com.dreamcode.dreamthemes.themes.service.ThemeService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CommandHandler implements CommandExecutor,TabCompleter {
    private final ThemeService themeService;
    private final MessageService messageService;
    private final ConfigService configService;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("dreamthemes.use")) {
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("dreamthemes.reload")) {
                return false;
            }
            boolean success = configService.reloadConfig();
            messageService.sendMessage(sender, success ? "reload-success" : "reload-error");
            return true;
        }

        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                messageService.sendMessage(sender, "console-cannot-set-self");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("dreamthemes.set")) {
                return false;
            }
            String theme = themeService.findTheme(args[0]);
            if (theme == null && !args[0].equalsIgnoreCase("CLASSIC")) {
                messageService.sendMessage(sender, "invalid-theme");
                return true;
            }
            if (!themeService.hasThemePermission(player, args[0])) {
                messageService.sendMessage(sender, "cannot-edit-yourself");
                return true;
            }
            themeService.setTheme(player.getUniqueId(), args[0]);
            messageService.sendMessage(sender, "theme-set", args[0]);
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            if (!sender.hasPermission("dreamthemes.set.others")) {
                return false;
            }
            Player target = DreamThemes.getInstance().getServer().getPlayer(args[1]);
            if (target == null) {
                messageService.sendMessage(sender, "player-is-offline", args[2]);
                return true;
            }
            String theme = themeService.findTheme(args[2]);
            if (theme == null && !args[2].equalsIgnoreCase("CLASSIC")) {
                messageService.sendMessage(sender, "invalid-theme", args[2]);
                return true;
            }
            themeService.setTheme(target.getUniqueId(), args[2]);
            messageService.sendMessage(sender, "theme-set-others", args[2]);
            return true;
        }

        messageService.sendMessage(sender, "invalid-usage");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("dreamthemes.use")) {
            return new ArrayList<>();
        }

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("dreamthemes.set") && sender instanceof Player) {
                Player player = (Player) sender;
                for (String theme : configService.getThemes()) {
                    if (themeService.hasThemePermission(player, theme) || theme.equalsIgnoreCase("CLASSIC")) {
                        completions.add(theme);
                    }
                }
            }
            if (sender.hasPermission("dreamthemes.set.others")) {
                completions.add("set");
            }
            if (sender.hasPermission("dreamthemes.reload")) {
                completions.add("reload");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("set") &&
                sender.hasPermission("dreamthemes.set.others")) {
            for (Player player : DreamThemes.getInstance().getServer().getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("set") &&
                sender.hasPermission("dreamthemes.set.others")) {
            completions.addAll(configService.getThemes());
        }
        return completions;
    }
}