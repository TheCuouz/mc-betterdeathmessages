package com.cristian.betterdeathmessages.tracker;

import java.util.List;

public class KillStreakBroadcaster {

    private final List<Integer> thresholds;

    public KillStreakBroadcaster(List<Integer> thresholds) {
        this.thresholds = thresholds.stream().sorted().toList();
    }

    public boolean isMilestone(int streak) {
        return thresholds.contains(streak);
    }

    public int highestReached(int streak) {
        int best = -1;
        for (int t : thresholds) {
            if (streak >= t) best = t;
        }
        return best;
    }
}
