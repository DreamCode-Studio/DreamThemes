package com.dreamcode.dreamthemes.configs.service;

import java.util.List;

public interface ConfigService {
    List<String> getThemes();

    String getDatabaseType();

    String getMySQLHost();

    int getMySQLPort();

    String getMySQLDatabase();

    String getMySQLUsername();

    String getMySQLPassword();

    long getCheckInterval();

    List<String> getMessages(String key, String theme);

    boolean reloadConfig();

    long getCacheDurationSeconds();

    long getCacheMaximumSize();
}