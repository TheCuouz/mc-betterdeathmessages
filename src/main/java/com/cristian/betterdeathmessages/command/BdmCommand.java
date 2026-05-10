package com.cristian.betterdeathmessages.command;

import com.cristian.betterdeathmessages.BetterDeathMessagesPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BdmCommand implements CommandExecutor {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final BetterDeathMessagesPlugin plugin;

    public BdmCommand(BetterDeathMessagesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("bdm.admin")) {
            sender.sendMessage(MM.deserialize("<red>No tienes permiso.</red>"));
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            sender.sendMessage(MM.deserialize("<green>BetterDeathMessages recargado.</green>"));
            return true;
        }
        sender.sendMessage(MM.deserialize("<yellow>Uso: /bdm reload</yellow>"));
        return true;
    }
}
