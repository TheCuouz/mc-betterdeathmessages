package com.cristian.betterdeathmessages.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageWordingTest {

    @Test
    void anBeforeAVowel() {
        assertEquals("back to bed with an</gray> <aqua>{weapon}</aqua>",
            MessagePicker.fixArticle("back to bed with a</gray> <aqua>{weapon}</aqua>", "{weapon}", "Iron sword"));
        assertEquals("lost a fight to an {mob} in",
            MessagePicker.fixArticle("lost a fight to a {mob} in", "{mob}", "Evoker"));
    }

    @Test
    void aBeforeAConsonantEvenIfTheTemplateSaidAn() {
        assertEquals("slain by a</gray> <gray>{mob}</gray>",
            MessagePicker.fixArticle("slain by an</gray> <gray>{mob}</gray>", "{mob}", "Zombie"));
    }

    @Test
    void leavesOtherWordsAlone() {
        String t = "<red>{player}</red> <gray>had a bad day with</gray> {weapon}";
        assertEquals(t, MessagePicker.fixArticle(t, "{weapon}", "Iron sword"));
        assertEquals("Was a {mob}", MessagePicker.fixArticle("Was a {mob}", "{mob}", ""));
    }

    @Test
    void biomeKeysReadAsNames() {
        assertEquals("Lukewarm Ocean", MessagePicker.biomeName("lukewarm_ocean"));
        assertEquals("Plains", MessagePicker.biomeName("plains"));
    }
}
