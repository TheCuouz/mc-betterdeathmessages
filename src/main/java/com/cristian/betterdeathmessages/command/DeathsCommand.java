package com.cristian.betterdeathmessages.command;

import com.cristian.betterdeathmessages.BetterDeathMessagesPlugin;
import com.cristian.betterdeathmessages.model.PlayerDeathStats;
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

    public DeathsCommand(BetterDeathMessagesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        UUID target;
        String targetName;

        if (args.length >= 1) {
            OfflinePlayer op = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (op == null) {
                sender.sendMessage(MM.deserialize("<red>Jugador no encontrado: " + args[0] + "</red>"));
                return true;
            }
            target     = op.getUniqueId();
            targetName = op.getName() != null ? op.getName() : args[0];
        } else {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Uso: /deaths <jugador>");
                return true;
            }
            target     = player.getUniqueId();
            targetName = player.getName();
        }

        PlayerDeathStats stats = plugin.getDeathStatsService().get(target);
        double kdr = (double) stats.totalKills / Math.max(1, stats.totalDeaths);

        sender.sendMessage(MM.deserialize(
            "<gold>══ Stats de <white>" + targetName + "</white> ══</gold>\n" +
            "<yellow>Muertes totales: <white>" + stats.totalDeaths + "</white></yellow>\n" +
            "<yellow>Asesinatos:      <white>" + stats.totalKills  + "</white></yellow>\n" +
            "<yellow>KDR:             <white>" + String.format("%.2f", kdr) + "</white></yellow>\n" +
            "<yellow>Racha actual:    <white>" + stats.currentKillStreak  + "</white></yellow>\n" +
            "<yellow>Racha máxima:    <white>" + stats.longestKillStreak + "</white></yellow>"
        ));
        return true;
    }
}
