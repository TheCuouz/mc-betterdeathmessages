package com.cristian.betterdeathmessages;

import com.cristian.betterdeathmessages.cfg.ConfigManager;
import com.cristian.betterdeathmessages.cfg.MessageManager;
import com.cristian.betterdeathmessages.command.BdmCommand;
import com.cristian.betterdeathmessages.command.DeathsCommand;
import com.cristian.betterdeathmessages.integration.PapiHook;
import com.cristian.betterdeathmessages.lastwords.LastWordsCache;
import com.cristian.betterdeathmessages.listener.ChatCaptureListener;
import com.cristian.betterdeathmessages.listener.DeathListener;
import com.cristian.betterdeathmessages.message.MessagePicker;
import com.cristian.betterdeathmessages.service.DeathStatsService;
import com.cristian.betterdeathmessages.tracker.FirstDeathTracker;
import com.cristian.betterdeathmessages.tracker.KillStreakBroadcaster;
import com.cristian.betterdeathmessages.tracker.FirstBloodTracker;
import com.ttsstudio.sdk.PluginIdentity;
import com.ttsstudio.sdk.console.ConsoleBanner;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public final class BetterDeathMessagesPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private MessageManager messageManager;
    private DeathStatsService deathStatsService;
    private MessagePicker messagePicker;
    private FirstDeathTracker firstDeathTracker;
    private LastWordsCache lastWordsCache;
    private KillStreakBroadcaster killStreakBroadcaster;
    private FirstBloodTracker firstBloodTracker;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        configManager = new ConfigManager(this);
        configManager.reload();

        messageManager = new MessageManager(this, configManager);
        messageManager.reload();

        deathStatsService = new DeathStatsService(getDataFolder());
        deathStatsService.load();

        firstDeathTracker = new FirstDeathTracker();

        // Last Words cache + capture listener (privacy-first, public chat only).
        long cacheSeconds = configManager.lastWordsCacheSeconds();
        lastWordsCache = new LastWordsCache(cacheSeconds * 1000L);

        // MessagePicker is rebuilt here so it has the cache + lang templates.
        messagePicker = new MessagePicker(messageManager.getTemplates(), configManager.raw(), lastWordsCache);

        java.util.List<Integer> thresholds = configManager.killStreakThresholds();
        killStreakBroadcaster = thresholds.isEmpty()
            ? new KillStreakBroadcaster(java.util.List.of(3, 5, 10, 20))
            : new KillStreakBroadcaster(thresholds);
        firstBloodTracker = new FirstBloodTracker();

        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        if (configManager.lastWordsEnabled()) {
            getServer().getPluginManager()
                .registerEvents(new ChatCaptureListener(lastWordsCache), this);
            // Periodic janitor: drop expired entries every minute.
            getServer().getScheduler().runTaskTimer(
                this, lastWordsCache::purgeExpired, 20L * 60L, 20L * 60L);
        }

        var deathsCmd = getCommand("deaths");
        if (deathsCmd != null) deathsCmd.setExecutor(new DeathsCommand(this));

        var bdmCmd = getCommand("bdm");
        if (bdmCmd != null) bdmCmd.setExecutor(new BdmCommand(this));

        boolean papi = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (papi) {
            new PapiHook(this).register();
        }

        new Metrics(this, 12347);

        int templateCount = countDeathTemplates();

        ConsoleBanner.enable(this, PluginIdentity.of(this))
            .status(templateCount + " death message templates loaded")
            .hook(papi ? "PAPI" : null)
            .hook(configManager.lastWordsEnabled() ? "LastWords" : null)
            .ready(Duration.ofMillis(System.currentTimeMillis() - startTime))
            .emit();
    }

    @Override
    public void onDisable() {
        if (deathStatsService != null) {
            deathStatsService.save();
        }
        ConsoleBanner.disable(this, PluginIdentity.of(this)).emit();
    }

    public void reload() {
        configManager.reload();
        messageManager.reload();
        // Rebuild picker so it picks up the reloaded messages + plugin config.
        messagePicker = new MessagePicker(messageManager.getTemplates(), configManager.raw(), lastWordsCache);
    }

    private int countDeathTemplates() {
        int total = 0;
        ConfigurationSection root = messageManager.getTemplates().getConfigurationSection("messages");
        if (root == null) return 0;
        for (String key : root.getKeys(false)) {
            String path = "messages." + key;
            if (messageManager.getTemplates().isList(path)) {
                total += messageManager.getTemplates().getStringList(path).size();
            } else if (messageManager.getTemplates().isConfigurationSection(path)) {
                ConfigurationSection sub = messageManager.getTemplates().getConfigurationSection(path);
                if (sub != null) {
                    for (String subKey : sub.getKeys(false)) {
                        total += messageManager.getTemplates().getStringList(path + "." + subKey).size();
                    }
                }
            }
        }
        return total;
    }

    public ConfigManager getConfigManager()     { return configManager; }
    public MessageManager getMessages()         { return messageManager; }
    public DeathStatsService getDeathStatsService() { return deathStatsService; }
    public MessagePicker getMessagePicker()     { return messagePicker; }
    public FirstDeathTracker getFirstDeathTracker() { return firstDeathTracker; }
    public LastWordsCache getLastWordsCache()   { return lastWordsCache; }
    public KillStreakBroadcaster getKillStreakBroadcaster() { return killStreakBroadcaster; }
    public FirstBloodTracker getFirstBloodTracker()         { return firstBloodTracker; }
}
