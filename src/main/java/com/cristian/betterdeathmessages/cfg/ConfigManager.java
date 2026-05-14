package com.cristian.betterdeathmessages.cfg;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration cfg;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.cfg = plugin.getConfig();
    }

    public FileConfiguration raw() { return cfg; }

    /** Active locale (ISO-639-1 two-letter code, lowercase). Default "es". */
    public String language() { return cfg.getString("language", "es").toLowerCase(); }

    public boolean lastWordsEnabled()  { return cfg.getBoolean("last-words.enabled", true); }
    public long    lastWordsCacheSeconds() { return Math.max(1L, cfg.getLong("last-words.cache-seconds", 60L)); }
    public String  lastWordsFallback() { return cfg.getString("last-words.fallback", ""); }

    public java.util.List<String> globalBroadcastCauses() {
        return cfg.getStringList("global-broadcast.enabled-causes");
    }
    public boolean globalBroadcastHoverStats() { return cfg.getBoolean("global-broadcast.hover-stats", true); }

    public boolean firstDeathEnabled() { return cfg.getBoolean("first-death-of-day.enabled", true); }
}
