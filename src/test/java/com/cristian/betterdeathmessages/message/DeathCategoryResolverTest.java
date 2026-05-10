package com.cristian.betterdeathmessages.message;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeathCategoryResolverTest {

    @Test
    void pvpKill_returnsPvp() {
        assertEquals("pvp", DeathCategoryResolver.resolve(true, false, "", "ENTITY_ATTACK"));
    }

    @Test
    void mobKillZombie_returnsMobZombie() {
        assertEquals("mob.ZOMBIE", DeathCategoryResolver.resolve(false, true, "ZOMBIE", "ENTITY_ATTACK"));
    }

    @Test
    void mobKillEnderDragon_returnsMobEnderDragon() {
        assertEquals("mob.ENDER_DRAGON", DeathCategoryResolver.resolve(false, true, "ENDER_DRAGON", "ENTITY_ATTACK"));
    }

    @Test
    void fallDamage_returnsFall() {
        assertEquals("fall", DeathCategoryResolver.resolve(false, false, "", "FALL"));
    }

    @Test
    void lavaDamage_returnsLava() {
        assertEquals("lava", DeathCategoryResolver.resolve(false, false, "", "LAVA"));
    }

    @Test
    void drowningDamage_returnsDrown() {
        assertEquals("drown", DeathCategoryResolver.resolve(false, false, "", "DROWNING"));
    }

    @Test
    void voidDamage_returnsVoid() {
        assertEquals("void", DeathCategoryResolver.resolve(false, false, "", "VOID"));
    }

    @Test
    void pvpKillTakesPriorityOverMob() {
        assertEquals("pvp", DeathCategoryResolver.resolve(true, true, "ZOMBIE", "ENTITY_ATTACK"));
    }
}
