package com.cristian.betterdeathmessages.listener;

import com.cristian.betterdeathmessages.lastwords.LastWordsCache;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Captures the last public chat message of each player into a {@link LastWordsCache}.
 *
 * <p>Privacy-first policy: a chat event is considered public only if its
 * {@code viewers()} set is at least as large as the online roster (plus the
 * console audience). If any other plugin has restricted the audience to a
 * subset — whispers, channel chat, party chat, etc. — the message is silently
 * discarded.
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
        // Conservative public-chat detection:
        //   Bukkit.getOnlinePlayers() = every online player audience.
        //   +1 accounts for the console audience that Paper includes by default.
        // If viewers() is smaller, someone has narrowed the audience → not public.
        int onlineCount = Bukkit.getOnlinePlayers().size() + 1;
        if (event.viewers().size() < onlineCount) {
            return;
        }

        String plain = PLAIN.serialize(event.message());
        if (plain.isBlank()) return;

        cache.put(event.getPlayer().getUniqueId(), plain);
    }
}
