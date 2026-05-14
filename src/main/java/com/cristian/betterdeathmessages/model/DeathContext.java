package com.cristian.betterdeathmessages.model;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public record DeathContext(
    Player victim,
    @Nullable Player playerKiller,
    @Nullable Entity mobKiller,
    EntityDamageEvent.DamageCause cause,
    @Nullable ItemStack weapon,
    double fallDistance,
    String biome,
    Location deathLocation,
    String dimension   // "overworld", "nether", "the_end"
) {}
