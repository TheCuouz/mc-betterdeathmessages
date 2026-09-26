package com.cristian.betterdeathmessages.listener;

import com.cristian.betterdeathmessages.lastwords.LastWordsCache;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.Set;

/**
 * Captures the last public chat message of each player into a {@link LastWordsCache}.
 *
 * <p>Privacy-first policy: a chat event is considered public only if every
 * online player is among its {@code viewers()}. If any other plugin has
 * restricted the audience to a subset — whispers, local or staff channels,
 * party chat, etc. — the message is silently discarded. The console is not
 * counted: channel plugins rebuild the audience with players only.
 *
 * <p>Runs at {@link EventPriority#MONITOR} with {@code ignoreCancelled = true}
 * so we observe the final audience after all other plugins have had their say.
 */
public class ChatCaptureListener implements Listener {

    private static final PlainTextComponentSerializer PLAIN =
        PlainTextComponentSerializer.plainText();

    private final LastWordsCache cache;

    public ChatCaptureListener(LastWordsCache cache) {
        this.cache = cache;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        if (!isPublic(event.viewers(), Bukkit.getOnlinePlayers())) return;

        String plain = PLAIN.serialize(event.message());
        if (plain.isBlank()) return;

        cache.put(event.getPlayer().getUniqueId(), plain);
    }

    static boolean isPublic(Set<?> viewers, Collection<?> online) {
        return viewers.containsAll(online);
    }
}
