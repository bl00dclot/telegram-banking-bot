package com.discryptment.bot;

import java.util.HashSet;
import java.util.Set;

public class SessionManager {
    private final Set<Long> startedUsers = new HashSet<>();
    public void markStarted(long tgId) {
        startedUsers.add(tgId);
    }
    public boolean hasStarted(long tgId) {
        return startedUsers.contains(tgId);
    }

    public void resetStarted(long tgId) {
        startedUsers.remove(tgId);
    }
}
