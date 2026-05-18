package dev.uiweaver.runtime.sync;

import dev.uiweaver.api.sync.SyncType;

public record SyncEntry(String key, SyncType type, Object value) {

    public int asInt() { return (Integer) value; }
    public long asLong() { return (Long) value; }
    public boolean asBoolean() { return (Boolean) value; }
    public float asFloat() { return (Float) value; }
    public String asString() { return (String) value; }
}