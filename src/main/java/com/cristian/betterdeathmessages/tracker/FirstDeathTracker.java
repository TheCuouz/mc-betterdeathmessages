package com.cristian.betterdeathmessages.tracker;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FirstDeathTracker {

    private final Set<UUID> diedToday = ConcurrentHashMap.newKeySet();
    private volatile LocalDate currentDate = LocalDate.now();

    public boolean isFirstTodayAndRecord(UUID uuid) {
        refreshIfNewDay();
        return diedToday.add(uuid);
    }

    private void refreshIfNewDay() {
        LocalDate today = LocalDate.now();
        if (!today.equals(currentDate)) {
            currentDate = today;
            diedToday.clear();
        }
    }
}
