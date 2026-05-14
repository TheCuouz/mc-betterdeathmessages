package com.cristian.betterdeathmessages.lastwords;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class LastWordsCacheTest {

    @Test
    void put_thenGet_returnsValueWithinTtl() {
        AtomicLong clock = new AtomicLong(1_000L);
        LastWordsCache cache = new LastWordsCache(5_000L, clock::get);
        UUID uuid = UUID.randomUUID();

        cache.put(uuid, "hola");

        // Same instant
        assertEquals(Optional.of("hola"), cache.get(uuid));

        // Just inside TTL boundary
        clock.set(1_000L + 5_000L);
        assertEquals(Optional.of("hola"), cache.get(uuid));
    }

    @Test
    void put_secondOverwritesFirst() {
        AtomicLong clock = new AtomicLong(0L);
        LastWordsCache cache = new LastWordsCache(60_000L, clock::get);
        UUID uuid = UUID.randomUUID();

        cache.put(uuid, "primero");
        clock.set(1_000L);
        cache.put(uuid, "segundo");

        assertEquals(Optional.of("segundo"), cache.get(uuid));
    }

    @Test
    void get_afterTtlExpires_returnsEmpty() {
        AtomicLong clock = new AtomicLong(0L);
        LastWordsCache cache = new LastWordsCache(5_000L, clock::get);
        UUID uuid = UUID.randomUUID();

        cache.put(uuid, "hola");

        // ttlMillis + 1 past the put timestamp → expired
        clock.set(5_001L);
        assertEquals(Optional.empty(), cache.get(uuid));
    }

    @Test
    void get_unknownUuid_returnsEmpty() {
        LastWordsCache cache = new LastWordsCache(5_000L, () -> 0L);
        assertEquals(Optional.empty(), cache.get(UUID.randomUUID()));
    }

    @Test
    void purgeExpired_removesEntriesPastTtl() {
        AtomicLong clock = new AtomicLong(0L);
        LastWordsCache cache = new LastWordsCache(5_000L, clock::get);
        UUID expired = UUID.randomUUID();
        UUID fresh = UUID.randomUUID();

        cache.put(expired, "old");
        clock.set(4_000L);
        cache.put(fresh, "new");

        // Advance so 'expired' is past TTL but 'fresh' is still inside
        clock.set(6_000L);
        cache.purgeExpired();

        assertEquals(Optional.empty(), cache.get(expired));
        assertEquals(Optional.of("new"), cache.get(fresh));
    }
}
