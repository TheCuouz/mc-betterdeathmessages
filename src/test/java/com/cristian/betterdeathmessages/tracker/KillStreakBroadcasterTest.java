package com.cristian.betterdeathmessages.tracker;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class KillStreakBroadcasterTest {

    private KillStreakBroadcaster broadcaster() {
        return new KillStreakBroadcaster(List.of(3, 5, 10, 20));
    }

    @Test
    void streak3_isMilestone() {
        assertTrue(broadcaster().isMilestone(3));
    }

    @Test
    void streak4_isNotMilestone() {
        assertFalse(broadcaster().isMilestone(4));
    }

    @Test
    void streak10_isMilestone() {
        assertTrue(broadcaster().isMilestone(10));
    }

    @Test
    void streak2_highestReachedIsMinusOne() {
        assertEquals(-1, broadcaster().highestReached(2));
    }

    @Test
    void streak3_highestReachedIs3() {
        assertEquals(3, broadcaster().highestReached(3));
    }

    @Test
    void streak7_highestReachedIs5() {
        assertEquals(5, broadcaster().highestReached(7));
    }

    @Test
    void streak20_highestReachedIs20() {
        assertEquals(20, broadcaster().highestReached(20));
    }
}
