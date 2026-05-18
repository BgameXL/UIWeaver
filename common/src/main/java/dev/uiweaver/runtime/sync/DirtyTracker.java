package dev.uiweaver.runtime.sync;

import dev.uiweaver.api.sync.SyncDeclaration;
import dev.uiweaver.api.sync.SyncType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DirtyTracker {

    private final Map<String, Object> lastValues = new HashMap<>();

    public boolean isDirty(SyncDeclaration<?> decl) {
        Object current = decl.getServerSource().get();
        Object last    = lastValues.get(decl.getKey());
        return !equal(decl.getType(), current, last);
    }

    public void markClean(SyncDeclaration<?> decl) {
        Object current = decl.getServerSource().get();
        if (current instanceof List<?> list) {
            lastValues.put(decl.getKey(), List.copyOf(list));
        } else {
            lastValues.put(decl.getKey(), current);
        }
    }

    public void markAllDirty() {
        lastValues.clear();
    }

    public List<SyncDeclaration<?>> collectDirty(List<SyncDeclaration<?>> decls) {
        return decls.stream().filter(this::isDirty).toList();
    }

    private static boolean equal(SyncType type, Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (type == SyncType.STRING_LIST || type == SyncType.NBT_LIST) {
            if (!(a instanceof List<?> la) || !(b instanceof List<?> lb)) return false;
            if (la.size() != lb.size()) return false;
            for (int i = 0; i < la.size(); i++) {
                if (!Objects.equals(la.get(i), lb.get(i))) return false;
            }
            return true;
        }
        return Objects.equals(a, b);
    }
}