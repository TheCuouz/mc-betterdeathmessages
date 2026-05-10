package com.cristian.betterdeathmessages.model;

import java.util.HashMap;
import java.util.Map;

public class PlayerDeathStats {
    public Map<String, Integer> deathsByCategory = new HashMap<>();
    public int totalKills        = 0;
    public int totalDeaths       = 0;
    public int longestKillStreak = 0;
    public int currentKillStreak = 0;
}
