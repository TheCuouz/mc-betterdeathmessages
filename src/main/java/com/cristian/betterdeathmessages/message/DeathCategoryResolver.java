package com.cristian.betterdeathmessages.message;

public class DeathCategoryResolver {

    public static String resolve(boolean hasPvpKiller, boolean hasMobKiller,
                                  String entityType, String causeName) {
        if (hasPvpKiller) return "pvp";
        if (hasMobKiller) return "mob." + entityType;
        return switch (causeName) {
            case "FALL"                                  -> "fall";
            case "LAVA"                                  -> "lava";
            case "FIRE", "FIRE_TICK", "HOT_FLOOR"        -> "fire";
            case "DROWNING"                              -> "drown";
            case "VOID"                                  -> "void";
            case "ENTITY_EXPLOSION", "BLOCK_EXPLOSION"   -> "explosion";
            case "MAGIC"                                 -> "magic";
            case "LIGHTNING"                             -> "lightning";
            case "CRAMMING"                              -> "cramming";
            case "FREEZE"                                -> "freeze";
            case "SUFFOCATION"                           -> "suffocation";
            case "CONTACT"                               -> "cactus";
            case "SONIC_BOOM"                            -> "sonic_boom";
            case "STARVATION"                            -> "starvation";
            case "POISON"                                -> "poison";
            case "WITHER"                                -> "wither_effect";
            default                                      -> "unknown";
        };
    }
}
