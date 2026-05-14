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
        if (args.length >= 1 && args[0].equalsIgnoreCase("top")) {
            boolean byKills = args.length < 2 || args[1].equalsIgnoreCase("killers");
            int page = args.length >= 3 ? parseIntSafe(args[2], 1) : 1;
            showLeaderboard(sender, byKills, page);
            return true;
        }

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

    private void showLeaderboard(CommandSender sender, boolean byKills, int page) {
        var all = plugin.getDeathStatsService().getAllStats();
        var sorted = all.entrySet().stream()
            .sorted((a, b) -> byKills
                ? Integer.compare(b.getValue().totalKills, a.getValue().totalKills)
                : Integer.compare(b.getValue().totalDeaths, a.getValue().totalDeaths))
            .toList();

        int pageSize = 10;
        int start = (page - 1) * pageSize;

        MiniMessage mm = MiniMessage.miniMessage();
        String headerKey = byKills ? "leaderboard.killers-header" : "leaderboard.deaths-header";
        String headerRaw = plugin.getMessages().getTemplates().getString(headerKey, "");
        if (!headerRaw.isEmpty()) sender.sendMessage(mm.deserialize(headerRaw));

        if (sorted.isEmpty() || start >= sorted.size()) {
            String emptyRaw = plugin.getMessages().getTemplates().getString("leaderboard.empty", "<gray>No data yet.</gray>");
            sender.sendMessage(mm.deserialize(emptyRaw));
            return;
        }

        int end = Math.min(start + pageSize, sorted.size());
        for (int i = start; i < end; i++) {
            var entry = sorted.get(i);
            String name = null;
            // Try to find the player in online players first (non-blocking)
            org.bukkit.entity.Player online = Bukkit.getPlayer(entry.getKey());
            if (online != null) {
                name = online.getName();
            }
            // Fall back to cached offline player lookup by name (avoids blocking I/O)
            if (name == null) {
                name = entry.getKey().toString().substring(0, 8);
            }
            int value = byKills ? entry.getValue().totalKills : entry.getValue().totalDeaths;
            String entryRaw = plugin.getMessages().getTemplates()
                .getString("leaderboard.entry", "<gray>{pos}. {player} — {value}</gray>")
                .replace("{pos}", String.valueOf(i + 1))
                .replace("{player}", name)
                .replace("{value}", String.valueOf(value));
            sender.sendMessage(mm.deserialize(entryRaw));
        }
    }

    private int parseIntSafe(String s, int def) {
        try { return Math.max(1, Integer.parseInt(s)); } catch (NumberFormatException e) { return def; }
    }
}
