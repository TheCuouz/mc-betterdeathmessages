package com.cristian.betterdeathmessages.command;

import com.cristian.betterdeathmessages.BetterDeathMessagesPlugin;
import com.ttsstudio.sdk.PluginIdentity;
import com.ttsstudio.sdk.chat.ChatPrefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BdmCommand implements CommandExecutor {

    private final BetterDeathMessagesPlugin plugin;
    private final PluginIdentity identity;

    public BdmCommand(BetterDeathMessagesPlugin plugin) {
        this.plugin = plugin;
        this.identity = PluginIdentity.of(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("bdm.admin")) {
            ChatPrefix.error(sender, identity, plugin.getMessages().get("command.no-permission"));
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            ChatPrefix.success(sender, identity, plugin.getMessages().get("command.reload-success"));
            return true;
        }
        ChatPrefix.warn(sender, identity, plugin.getMessages().get("command.usage-bdm"));
        return true;
    }
}
