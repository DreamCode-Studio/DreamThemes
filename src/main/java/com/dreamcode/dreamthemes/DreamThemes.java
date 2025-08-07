package com.dreamcode.dreamthemes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.dreamcode.dreamthemes.api.ThemeAPI;
import com.dreamcode.dreamthemes.api.impl.ThemeAPIImpl;
import com.dreamcode.dreamthemes.commands.CommandHandler;
import com.dreamcode.dreamthemes.configs.ConfigManager;
import com.dreamcode.dreamthemes.configs.service.ConfigService;
import com.dreamcode.dreamthemes.database.service.DatabaseService;
import com.dreamcode.dreamthemes.database.types.MySQLDatabaseManager;
import com.dreamcode.dreamthemes.database.types.SQLiteDatabaseManager;
import com.dreamcode.dreamthemes.messages.MessageHandler;
import com.dreamcode.dreamthemes.messages.service.MessageService;
import com.dreamcode.dreamthemes.themes.ThemeManager;
import com.dreamcode.dreamthemes.themes.check.ThemeCheckTask;
import com.dreamcode.dreamthemes.themes.service.ThemeService;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
@Getter
public final class DreamThemes extends JavaPlugin {

    @Getter
    private static DreamThemes instance;
    private ConfigService configService;
    private DatabaseService databaseService;
    private ThemeService themeService;
    private MessageService messageService;

    @Override
    public void onEnable() {
        instance = this;
        configService = new ConfigManager(this);
        databaseService = configService.getDatabaseType().equalsIgnoreCase("sqlite") ?
                new SQLiteDatabaseManager(this) : new MySQLDatabaseManager(configService);
        themeService = new ThemeManager(databaseService, configService);
        messageService = new MessageHandler(configService, themeService,this);

        getServer().getServicesManager().register(ThemeAPI.class, new ThemeAPIImpl(themeService), this, ServicePriority.Normal);

        databaseService.connect();
        getCommand("dreamthemes").setExecutor(new CommandHandler(themeService, messageService,configService));
        getCommand("dreamthemes").setTabCompleter(new CommandHandler(themeService, messageService,configService));

        new ThemeCheckTask(this, themeService, configService).start();
    }

    @Override
    public void onDisable() {
        databaseService.disconnect();
    }

}
