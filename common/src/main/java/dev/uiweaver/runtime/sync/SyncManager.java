package dev.uiweaver.runtime.sync;

import dev.uiweaver.api.sync.SyncDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncManager {

    private final List<SyncDeclaration<?>> declarations;
    private final DirtyTracker tracker = new DirtyTracker();
    private final Map<String, Long> lastSentTime = new HashMap<>();
    private final List<SyncEntry> pending = new ArrayList<>();

    public SyncManager(List<SyncDeclaration<?>> declarations) {
        this.declarations = declarations;
    }

    public void tick() {
        pending.clear();
        long now = System.currentTimeMillis();

        for (SyncDeclaration<?> decl : declarations) {
            if (!tracker.isDirty(decl)) continue;

            int debounce = decl.getRateMs();
            if (debounce > 0) {
                long last = lastSentTime.getOrDefault(decl.getKey(), 0L);
                if (now - last < debounce) continue;
            }

            Object value = decl.getServerSource().get();
            pending.add(new SyncEntry(decl.getKey(), decl.getType(), value));
            tracker.markClean(decl);
            lastSentTime.put(decl.getKey(), now);
        }
    }

    public boolean hasPending() {
        return !pending.isEmpty();
    }

    public List<SyncEntry> drainPending() {
        List<SyncEntry> result = List.copyOf(pending);
        pending.clear();
        return result;
    }

    public void forceFullSync() {
        tracker.markAllDirty();
        lastSentTime.clear();
    }
}