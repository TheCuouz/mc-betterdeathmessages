package com.cristian.betterdeathmessages;

import org.bukkit.plugin.java.JavaPlugin;

public final class BetterDeathMessagesPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getSLF4JLogger().info("BetterDeathMessages enabled.");
    }

    @Override
    public void onDisable() {
        getSLF4JLogger().info("BetterDeathMessages disabled.");
    }
}
