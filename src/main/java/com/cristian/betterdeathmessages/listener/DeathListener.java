package com.cristian.betterdeathmessages.listener;

import com.cristian.betterdeathmessages.BetterDeathMessagesPlugin;
import com.cristian.betterdeathmessages.message.DeathCategoryResolver;
import com.cristian.betterdeathmessages.model.DeathContext;
import com.cristian.betterdeathmessages.model.PlayerDeathStats;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

        String dimension = switch (victim.getWorld().getEnvironment()) {
            case NETHER   -> "nether";
            case THE_END  -> "the_end";
            case NORMAL   -> "overworld";
            default       -> victim.getWorld().getName().toLowerCase();
        };

        double inventoryValue = plugin.isVaultAvailable()
            ? plugin.getVaultHook().estimateInventoryValue(victim.getInventory().getContents())
            : 0.0;

        DeathContext ctx = new DeathContext(
            victim,
            playerKiller,
            mobKiller,
            cause,
            weapon,
            victim.getFallDistance(),
            victim.getWorld().getBiome(victim.getLocation()).getKey().getKey(),
            victim.getLocation(),
            dimension,
            inventoryValue
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

        // Radius-filtered broadcast
        int broadcastRadius = plugin.getConfigManager().broadcastRadius();
        if (broadcastRadius > 0) {
            // Override Paper's global broadcast with a radius-limited one
            event.deathMessage(null);
            Location deathLoc = victim.getLocation();
            Component finalMsg = message;
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.getWorld().equals(deathLoc.getWorld())
                        && online.getLocation().distanceSquared(deathLoc) <= (long) broadcastRadius * broadcastRadius) {
                    online.sendMessage(finalMsg);
                }
            }
        }

        // Death sound to nearby players
        if (plugin.getConfigManager().deathSoundEnabled()) {
            String soundName = plugin.getConfigManager().deathSoundType();
            float volume = plugin.getConfigManager().deathSoundVolume();
            float pitch  = plugin.getConfigManager().deathSoundPitch();
            int soundRadius = plugin.getConfigManager().deathSoundRadius();
            Location deathLoc = victim.getLocation();
            org.bukkit.Sound sound = null;
            try {
                sound = org.bukkit.Sound.valueOf(soundName);
            } catch (IllegalArgumentException e) {
                plugin.getSLF4JLogger().warn("Invalid death-sound.sound value: {}", soundName);
            }
            if (sound != null) {
                final org.bukkit.Sound finalSound = sound;
                final Location finalLoc = deathLoc;
                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (online.getWorld().equals(finalLoc.getWorld())
                            && online.getLocation().distanceSquared(finalLoc) <= (long) soundRadius * soundRadius) {
                        online.playSound(finalLoc, finalSound, volume, pitch);
                    }
                }
            }
        }

        final Player finalKiller   = playerKiller;
        final String finalCategory = category;
        final UUID victimUuid      = victim.getUniqueId();
        final String victimName    = victim.getName();
        CompletableFuture.runAsync(() -> {
            plugin.getDeathStatsService().record(victimUuid, finalCategory);

            if (finalKiller != null) {
                plugin.getDeathStatsService().recordKill(finalKiller.getUniqueId());
                int streak = plugin.getDeathStatsService().get(finalKiller.getUniqueId()).currentKillStreak;

                // Kill streak milestone broadcast
                if (plugin.getConfigManager().killStreakEnabled()
                        && plugin.getKillStreakBroadcaster().isMilestone(streak)) {
                    String streakKey = String.valueOf(streak);
                    String rawTemplate = plugin.getMessages().getTemplates()
                        .getString("kill-streak." + streakKey, "");
                    if (!rawTemplate.isEmpty()) {
                        String msg = rawTemplate.replace("{killer}", finalKiller.getName());
                        Bukkit.getScheduler().runTask(plugin,
                            () -> Bukkit.broadcast(MM.deserialize(msg)));
                    }
                }

                // First blood of the day
                if (plugin.getConfigManager().firstBloodEnabled()
                        && plugin.getFirstBloodTracker().isFirstTodayAndRecord()) {
                    String raw = plugin.getMessages().getTemplates()
                        .getString("first-blood.message", "");
                    if (!raw.isEmpty()) {
                        String msg = raw
                            .replace("{killer}", finalKiller.getName())
                            .replace("{victim}", victimName);
                        Bukkit.getScheduler().runTask(plugin,
                            () -> Bukkit.broadcast(MM.deserialize(msg)));
                    }
                }
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
            if (c.equalsIgnoreCase(category)) return true;
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
