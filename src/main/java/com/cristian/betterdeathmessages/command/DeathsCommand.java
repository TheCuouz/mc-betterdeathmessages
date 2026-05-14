package com.cristian.betterdeathmessages.command;

import com.cristian.betterdeathmessages.BetterDeathMessagesPlugin;
import com.cristian.betterdeathmessages.model.PlayerDeathStats;
import com.ttsstudio.sdk.PluginIdentity;
import com.ttsstudio.sdk.chat.ChatPrefix;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DeathsCommand implements CommandExecutor {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final BetterDeathMessagesPlugin plugin;
    private final PluginIdentity identity;

    public DeathsCommand(BetterDeathMessagesPlugin plugin) {
        this.plugin = plugin;
        this.identity = PluginIdentity.of(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        UUID target;
        String targetName;

        if (args.length >= 1) {
            OfflinePlayer op = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (op == null) {
                ChatPrefix.error(sender, identity,
                    plugin.getMessages().get("command.player-not-found", "player", args[0]));
                return true;
            }
            target     = op.getUniqueId();
            targetName = op.getName() != null ? op.getName() : args[0];
        } else {
            if (!(sender instanceof Player player)) {
                ChatPrefix.warn(sender, identity, plugin.getMessages().get("command.usage-deaths"));
                return true;
            }
            target     = player.getUniqueId();
            targetName = player.getName();
        }

        PlayerDeathStats stats = plugin.getDeathStatsService().get(target);
        double kdr = (double) stats.totalKills / Math.max(1, stats.totalDeaths);

        ChatPrefix.send(sender, identity,
            plugin.getMessages().get("stats.deaths-header", "player", targetName) + "\n" +
            plugin.getMessages().get("stats.total-deaths",  "value", String.valueOf(stats.totalDeaths)) + "\n" +
            plugin.getMessages().get("stats.kills",         "value", String.valueOf(stats.totalKills))  + "\n" +
            plugin.getMessages().get("stats.kdr",           "value", String.format("%.2f", kdr))        + "\n" +
            plugin.getMessages().get("stats.kill-streak-current", "value", String.valueOf(stats.currentKillStreak)) + "\n" +
            plugin.getMessages().get("stats.kill-streak-max",     "value", String.valueOf(stats.longestKillStreak))
        );
        return true;
    }
}
