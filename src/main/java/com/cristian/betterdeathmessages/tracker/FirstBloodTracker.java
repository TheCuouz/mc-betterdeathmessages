package com.cristian.betterdeathmessages.tracker;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

public class FirstBloodTracker {

    private final AtomicReference<LocalDate> lastBloodDate = new AtomicReference<>(null);

    public boolean isFirstTodayAndRecord() {
        LocalDate today = LocalDate.now();
        LocalDate prev = lastBloodDate.get();
        if (today.equals(prev)) return false;
        return lastBloodDate.compareAndSet(prev, today);
    }
}
