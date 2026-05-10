package com.cristian.betterdeathmessages.message;

public class DeathCategoryResolver {

    public static String resolve(boolean hasPvpKiller, boolean hasMobKiller,
                                  String entityType, String causeName) {
        if (hasPvpKiller) return "pvp";
        if (hasMobKiller) return "mob." + entityType;
        return switch (causeName) {
            case "FALL"                            -> "fall";
            case "LAVA"                            -> "lava";
            case "FIRE", "FIRE_TICK", "HOT_FLOOR" -> "lava";
            case "DROWNING"                        -> "drown";
            case "VOID"                            -> "void";
            default                                -> "unknown";
        };
    }
}
