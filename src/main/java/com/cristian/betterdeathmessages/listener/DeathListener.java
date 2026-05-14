package com.cristian.betterdeathmessages.listener;

import com.cristian.betterdeathmessages.BetterDeathMessagesPlugin;
import com.cristian.betterdeathmessages.message.DeathCategoryResolver;
import com.cristian.betterdeathmessages.model.DeathContext;
import com.cristian.betterdeathmessages.model.PlayerDeathStats;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DeathListener implements Listener {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final BetterDeathMessagesPlugin plugin;

    public DeathListener(BetterDeathMessagesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        EntityDamageEvent lastDmg = victim.getLastDamageCause();

        Player playerKiller = null;
        Entity mobKiller    = null;
        ItemStack weapon    = null;

        if (lastDmg instanceof EntityDamageByEntityEvent byEntity) {
            Entity damager = byEntity.getDamager();
            if (damager instanceof Player p) {
                playerKiller = p;
                weapon = p.getInventory().getItemInMainHand().clone();
            } else if (damager instanceof Projectile proj && proj.getShooter() instanceof Player p) {
                playerKiller = p;
                weapon = p.getInventory().getItemInMainHand().clone();
            } else {
                mobKiller = damager;
            }
        }

        EntityDamageEvent.DamageCause cause = lastDmg != null
            ? lastDmg.getCause()
            : EntityDamageEvent.DamageCause.CUSTOM;

        DeathContext ctx = new DeathContext(
            victim,
            playerKiller,
            mobKiller,
            cause,
            weapon,
            victim.getFallDistance(),
            victim.getWorld().getBiome(victim.getLocation()).getKey().getKey(),
            victim.getLocation()
        );

        String category = DeathCategoryResolver.resolve(
            playerKiller != null,
            mobKiller    != null,
            mobKiller    != null ? mobKiller.getType().name() : "",
            cause.name()
        );

        Component message = plugin.getMessagePicker().pick(ctx);

        List<String> broadcastCauses = plugin.getConfigManager().globalBroadcastCauses();
        if (isBroadcastCause(category, broadcastCauses)
                && plugin.getConfigManager().globalBroadcastHoverStats()) {
            PlayerDeathStats victimStats = plugin.getDeathStatsService().get(victim.getUniqueId());
            message = message.hoverEvent(HoverEvent.showText(buildHoverText(victim, victimStats)));
        }

        event.deathMessage(message);

        final Player finalKiller   = playerKiller;
        final String finalCategory = category;
        final UUID victimUuid      = victim.getUniqueId();
        CompletableFuture.runAsync(() -> {
            plugin.getDeathStatsService().record(victimUuid, finalCategory);
            if (finalKiller != null) {
                plugin.getDeathStatsService().recordKill(finalKiller.getUniqueId());
            }
            plugin.getDeathStatsService().save();
        });

        if (plugin.getFirstDeathTracker().isFirstTodayAndRecord(victim.getUniqueId())
                && plugin.getConfigManager().firstDeathEnabled()) {
            String msg = plugin.getMessages()
                .get("first-death-of-day.message", "player", victim.getName());
            if (!msg.isEmpty()) {
                Bukkit.broadcast(MM.deserialize(msg));
            }
        }
    }

    private boolean isBroadcastCause(String category, List<String> enabledCauses) {
        for (String c : enabledCauses) {
            if (c.equalsIgnoreCase("pvp") && category.equals("pvp")) return true;
            if (category.equalsIgnoreCase("mob." + c)) return true;
        }
        return false;
    }

    private Component buildHoverText(Player victim, PlayerDeathStats stats) {
        double kdr = (double) stats.totalKills / Math.max(1, stats.totalDeaths);
        return MM.deserialize(
            plugin.getMessages().get("stats.header", "player", victim.getName()) + "\n" +
            plugin.getMessages().get("stats.total-deaths", "value", String.valueOf(stats.totalDeaths)) + "\n" +
            plugin.getMessages().get("stats.kills",        "value", String.valueOf(stats.totalKills))  + "\n" +
            plugin.getMessages().get("stats.kdr",          "value", String.format("%.2f", kdr))        + "\n" +
            plugin.getMessages().get("stats.kill-streak-max", "value", String.valueOf(stats.longestKillStreak))
        );
    }
}
