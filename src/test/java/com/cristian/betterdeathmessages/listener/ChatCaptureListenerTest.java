package com.cristian.betterdeathmessages.listener;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChatCaptureListenerTest {

    private static final List<String> ONLINE = List.of("Alex", "Steve");

    @Test
    void paperDefaultAudience_everyPlayerPlusConsole_isPublic() {
        assertTrue(ChatCaptureListener.isPublic(Set.of("Alex", "Steve", "console"), ONLINE));
    }

    // A channel plugin (ChattyChannels) rebuilds the audience with players only.
    @Test
    void channelPluginGlobalChat_playersWithoutConsole_isPublic() {
        assertTrue(ChatCaptureListener.isPublic(Set.of("Alex", "Steve"), ONLINE));
    }

    @Test
    void localOrPrivateChat_missingAPlayer_isNotPublic() {
        assertFalse(ChatCaptureListener.isPublic(Set.of("Alex", "console"), ONLINE));
    }
}
