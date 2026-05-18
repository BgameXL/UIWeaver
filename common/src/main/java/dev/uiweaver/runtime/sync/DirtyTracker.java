package dev.uiweaver.runtime.sync;

import dev.uiweaver.api.sync.SyncDeclaration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DirtyTracker {

    private final Map<String, Object> lastValues = new HashMap<>();

    public boolean isDirty(SyncDeclaration<?> decl) {
        Object current = decl.getServerSource().get();
        Object last = lastValues.get(decl.getKey());
        return !Objects.equals(current, last);
    }

    public void markClean(SyncDeclaration<?> decl) {
        lastValues.put(decl.getKey(), decl.getServerSource().get());
    }

    public void markAllDirty() {
        lastValues.clear();
    }

    public List<SyncDeclaration<?>> collectDirty(List<SyncDeclaration<?>> decls) {
        return decls.stream().filter(this::isDirty).toList();
    }
}