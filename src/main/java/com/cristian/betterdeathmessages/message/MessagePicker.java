package com.cristian.betterdeathmessages.message;

import com.cristian.betterdeathmessages.model.DeathContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class MessagePicker {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final Random RANDOM = new Random();

    private final FileConfiguration config;

    public MessagePicker(FileConfiguration config) {
        this.config = config;
    }

    public Component pick(DeathContext ctx) {
        String category = DeathCategoryResolver.resolve(
            ctx.playerKiller() != null,
            ctx.mobKiller()    != null,
            ctx.mobKiller()    != null ? ctx.mobKiller().getType().name() : "",
            ctx.cause().name()
        );

        List<String> templates = getTemplates(category);
        if (templates.isEmpty()) templates = config.getStringList("messages.unknown");
        if (templates.isEmpty()) return Component.text(ctx.victim().getName() + " died.");

        String template = templates.get(RANDOM.nextInt(templates.size()));
        String rendered = template
            .replace("<player>",   ctx.victim().getName())
            .replace("<killer>",   ctx.playerKiller() != null ? ctx.playerKiller().getName() : "")
            .replace("<weapon>",   weaponName(ctx.weapon()))
            .replace("<mob>",      ctx.mobKiller() != null ? formatMob(ctx.mobKiller().getType().name()) : "")
            .replace("<distance>", String.format("%.0f", ctx.fallDistance()))
            .replace("<biome>",    ctx.biome());

        return MM.deserialize(rendered);
    }

    private List<String> getTemplates(String category) {
        if (category.startsWith("mob.")) {
            String mobType = category.substring(4);
            List<String> specific = config.getStringList("messages.mob." + mobType);
            if (!specific.isEmpty()) return specific;
            return config.getStringList("messages.mob.DEFAULT");
        }
        return config.getStringList("messages." + category);
    }

    private String weaponName(ItemStack item) {
        if (item == null || item.getType().isAir()) return "manos";
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
