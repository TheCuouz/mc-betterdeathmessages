package com.cristian.betterdeathmessages;

import com.cristian.betterdeathmessages.command.BdmCommand;
import com.cristian.betterdeathmessages.command.DeathsCommand;
import com.cristian.betterdeathmessages.integration.PapiHook;
import com.cristian.betterdeathmessages.listener.DeathListener;
import com.cristian.betterdeathmessages.message.MessagePicker;
import com.cristian.betterdeathmessages.service.DeathStatsService;
import com.cristian.betterdeathmessages.tracker.FirstDeathTracker;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class BetterDeathMessagesPlugin extends JavaPlugin {

    private DeathStatsService deathStatsService;
    private MessagePicker messagePicker;
    private FirstDeathTracker firstDeathTracker;
    private FileConfiguration messagesConfig;

    @Override
    public void onEnable() {
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

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PapiHook(this).register();
            getSLF4JLogger().info("PlaceholderAPI expansion registered.");
        }

        new Metrics(this, 12347);

        getSLF4JLogger().info("BetterDeathMessages enabled.");
    }

    @Override
    public void onDisable() {
        if (deathStatsService != null) {
            deathStatsService.save();
        }
        getSLF4JLogger().info("BetterDeathMessages disabled.");
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

    public DeathStatsService getDeathStatsService() { return deathStatsService; }
    public MessagePicker getMessagePicker()         { return messagePicker; }
    public FirstDeathTracker getFirstDeathTracker() { return firstDeathTracker; }
    public FileConfiguration getMessagesConfig()    { return messagesConfig; }
}
