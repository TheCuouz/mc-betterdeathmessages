package com.cristian.betterdeathmessages.message;

import com.cristian.betterdeathmessages.lastwords.LastWordsCache;
import com.cristian.betterdeathmessages.model.DeathContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Picks a random death message template from the active locale's
 * {@code lang/<locale>.yml} (exposed via {@link com.cristian.betterdeathmessages.cfg.MessageManager#getTemplates()}).
 *
 * <p>Template YAML structure (flat list per category):</p>
 * <pre>
 * messages:
 *   fall:
 *     - "&lt;red&gt;{player}&lt;/red&gt; died ..."
 *   mob:
 *     ZOMBIE:
 *       - "..."
 * </pre>
 */
public class MessagePicker {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final Random RANDOM = new Random();

    /** The {@code lang/<locale>.yml} loaded as a FileConfiguration. */
    private final FileConfiguration templates;
    private final FileConfiguration pluginConfig;
    private final LastWordsCache lastWordsCache;

    public MessagePicker(FileConfiguration templates) {
        this(templates, null, null);
    }

    public MessagePicker(FileConfiguration templates,
                         FileConfiguration pluginConfig,
                         LastWordsCache lastWordsCache) {
        this.templates = templates;
        this.pluginConfig = pluginConfig;
        this.lastWordsCache = lastWordsCache;
    }

    public Component pick(DeathContext ctx) {
        String category = DeathCategoryResolver.resolve(
            ctx.playerKiller() != null,
            ctx.mobKiller()    != null,
            ctx.mobKiller()    != null ? ctx.mobKiller().getType().name() : "",
            ctx.cause().name()
        );

        List<String> templateList = getTemplates(category);
        if (templateList.isEmpty()) templateList = templates.getStringList("messages.unknown");
        if (templateList.isEmpty()) return Component.text(ctx.victim().getName() + " died.");

        String template = templateList.get(RANDOM.nextInt(templateList.size()));
        String rendered = template
            .replace("{player}",     ctx.victim().getName())
            .replace("{killer}",     ctx.playerKiller() != null ? ctx.playerKiller().getName() : "")
            .replace("{weapon}",     weaponName(ctx.weapon()))
            .replace("{mob}",        ctx.mobKiller() != null ? formatMob(ctx.mobKiller().getType().name()) : "")
            .replace("{distance}",   String.format("%.0f", ctx.fallDistance()))
            .replace("{biome}",      ctx.biome())
            .replace("{last_words}", resolveLastWords(ctx))
            .replace("{x}",          String.valueOf((int) ctx.deathLocation().getX()))
            .replace("{y}",          String.valueOf((int) ctx.deathLocation().getY()))
            .replace("{z}",          String.valueOf((int) ctx.deathLocation().getZ()))
            .replace("{dimension}",       ctx.dimension())
            .replace("{inventory_value}", String.format("%.0f", ctx.inventoryValue()));

        return MM.deserialize(rendered);
    }

    private String resolveLastWords(DeathContext ctx) {
        String fallback = pluginConfig != null
            ? pluginConfig.getString("last-words.fallback", "")
            : "";
        if (lastWordsCache == null) return fallback;

        Optional<String> cached = lastWordsCache.get(ctx.victim().getUniqueId());
        // Escape MiniMessage tag-openers so player chat cannot break formatting.
        return cached.map(MessagePicker::escapeForMiniMessage).orElse(fallback);
    }

    private static String escapeForMiniMessage(String raw) {
        return raw.replace("<", "\\<");
    }

    private List<String> getTemplates(String category) {
        if (category.startsWith("mob.")) {
            String mobType = category.substring(4);
            List<String> specific = templates.getStringList("messages.mob." + mobType);
            if (!specific.isEmpty()) return specific;
            return templates.getStringList("messages.mob.DEFAULT");
        }
        return templates.getStringList("messages." + category);
    }

    private String weaponName(ItemStack item) {
        if (item == null || item.getType().isAir()) return "";
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            return PlainTextComponentSerializer.plainText()
                .serialize(item.getItemMeta().displayName());
        }
        String raw = item.getType().name().replace("_", " ").toLowerCase();
        return Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
    }

    private String formatMob(String typeName) {
        String raw = typeName.replace("_", " ").toLowerCase();
        return Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
    }
}
