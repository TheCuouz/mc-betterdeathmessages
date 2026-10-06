package com.ttsstudio.betterdeathmessages.message;

import com.ttsstudio.betterdeathmessages.service.DeathStatsService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** One test per defect of the 5 October 2026 code review. */
class ReviewFixesTest {

    private static boolean hasClick(Component c) {
        if (c.clickEvent() != null) return true;
        for (Component child : c.children()) if (hasClick(child)) return true;
        return false;
    }

    @Test
    void aSwordNamedLikeATagIsShownAsItsName() {
        for (String name : List.of(
            "<click:run_command:'/op me'>Free rank",
            "\\<click:run_command:'/op me'>Free rank",
            "<red>[SERVER]")) {
            Component shown = MiniMessage.miniMessage().deserialize(
                "<gray>Steve was slain with {weapon}".replace("{weapon}", MessagePicker.escapeForMiniMessage(name)));
            assertFalse(hasClick(shown), name);
            assertTrue(PlainTextComponentSerializer.plainText().serialize(shown).endsWith(name), name);
        }
    }

    @Test
    void aDamagedStatsFileIsSetAsideInsteadOfStoppingThePlugin(@TempDir File folder) throws Exception {
        Files.writeString(new File(folder, "deaths.json").toPath(), "{ this is not json");
        DeathStatsService service = new DeathStatsService(folder);

        assertDoesNotThrow(service::load);

        assertTrue(new File(folder, "deaths.json.broken").exists());
        assertEquals(0, service.getAllStats().size());
    }
}
