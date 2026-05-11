package com.cristian.betterdeathmessages;

import com.cristian.betterdeathmessages.command.BdmCommand;
import com.cristian.betterdeathmessages.command.DeathsCommand;
import com.cristian.betterdeathmessages.integration.PapiHook;
import com.cristian.betterdeathmessages.listener.DeathListener;
import com.cristian.betterdeathmessages.message.MessagePicker;
import com.cristian.betterdeathmessages.service.DeathStatsService;
import com.cristian.betterdeathmessages.tracker.FirstDeathTracker;
import com.ttsstudio.sdk.PluginIdentity;
import com.ttsstudio.sdk.console.ConsoleBanner;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.time.Duration;

public final class BetterDeathMessagesPlugin extends JavaPlugin {

    private DeathStatsService deathStatsService;
    private MessagePicker messagePicker;
    private FirstDeathTracker firstDeathTracker;
    private FileConfiguration messagesConfig;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        saveDefaultConfig();
        saveResource("messages.yml", false);

        loadMessages();

        deathStatsService = new DeathStatsService(getDataFolder());
        deathStatsService.load();

        firstDeathTracker = new FirstDeathTracker();

        getServer().getPluginManager().registerEvents(new DeathListener(this), this);

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
        reloadConfig();
        loadMessages();
    }

    private void loadMessages() {
        File file = new File(getDataFolder(), "messages.yml");
        messagesConfig = YamlConfiguration.loadConfiguration(file);
        messagePicker  = new MessagePicker(messagesConfig);
    }

    private int countDeathTemplates() {
        int total = 0;
        ConfigurationSection root = messagesConfig.getConfigurationSection("messages");
        if (root == null) return 0;
        for (String key : root.getKeys(false)) {
            String path = "messages." + key;
            if (messagesConfig.isList(path)) {
                total += messagesConfig.getStringList(path).size();
            } else if (messagesConfig.isConfigurationSection(path)) {
                ConfigurationSection sub = messagesConfig.getConfigurationSection(path);
                if (sub != null) {
                    for (String subKey : sub.getKeys(false)) {
                        total += messagesConfig.getStringList(path + "." + subKey).size();
                    }
                }
            }
        }
        return total;
    }

    public DeathStatsService getDeathStatsService() { return deathStatsService; }
    public MessagePicker getMessagePicker()         { return messagePicker; }
    public FirstDeathTracker getFirstDeathTracker() { return firstDeathTracker; }
    public FileConfiguration getMessagesConfig()    { return messagesConfig; }
}
