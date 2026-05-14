package com.cristian.betterdeathmessages.lastwords;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

/**
 * Short-lived per-player cache for the most recent public chat message.
 * The cached message is used by the death listener to inject the
 * {@code {last_words}} token in death message templates.
 *
 * <p>Privacy: only public chat messages are inserted here (see
 * {@code ChatCaptureListener}); whispers and channel-restricted messages
 * never reach this cache. Entries expire automatically after {@code ttlMillis}.
 */
public class LastWordsCache {

    private final Map<UUID, Entry> data = new ConcurrentHashMap<>();
    private final long ttlMillis;
    private final LongSupplier clock;

    public LastWordsCache(long ttlMillis) {
        this(ttlMillis, System::currentTimeMillis);
    }

    LastWordsCache(long ttlMillis, LongSupplier clock) {
        this.ttlMillis = ttlMillis;
        this.clock = clock;
    }

    public void put(UUID uuid, String message) {
        data.put(uuid, new Entry(message, clock.getAsLong()));
    }

    public Optional<String> get(UUID uuid) {
        Entry e = data.get(uuid);
        if (e == null) return Optional.empty();
        if (clock.getAsLong() - e.timestamp() > ttlMillis) {
            data.remove(uuid);
            return Optional.empty();
        }
        return Optional.of(e.message());
    }

    public void purgeExpired() {
        long now = clock.getAsLong();
        data.entrySet().removeIf(e -> now - e.getValue().timestamp() > ttlMillis);
    }

    private record Entry(String message, long timestamp) {}
}
