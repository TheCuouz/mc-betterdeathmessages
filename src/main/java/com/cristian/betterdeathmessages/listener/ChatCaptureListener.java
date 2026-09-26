package com.cristian.betterdeathmessages.listener;

import com.cristian.betterdeathmessages.lastwords.LastWordsCache;
import com.ttsstudio.sdk.compat.Chat;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Captures the last public chat message of each player into a {@link LastWordsCache}.
 *
 * <p>Privacy-first policy: a chat message is considered public only if every
 * online player is in its audience. If any other plugin has restricted the
 * audience to a subset — whispers, local or staff channels, party chat, etc. —
 * the message is silently discarded. The console is not counted: channel
 * plugins rebuild the audience with players only.
 *
 * <p>{@link Chat#onPublicMessage} does the filtering on every server: Paper's own
 * chat event where it exists (that is where channel plugins narrow the audience),
 * Bukkit's elsewhere. It observes at MONITOR, after all other plugins have had their say.
 */
public class ChatCaptureListener implements Chat.PublicMessageHandler {

    private final LastWordsCache cache;

    public ChatCaptureListener(LastWordsCache cache) {
        this.cache = cache;
    }

    @Override
    public void onPublicMessage(Player sender, String plainText) {
        cache.put(sender.getUniqueId(), plainText);
    }

    static boolean isPublic(Collection<?> viewers, Collection<?> online) {
        return Chat.isPublic(viewers, online);
    }
}
