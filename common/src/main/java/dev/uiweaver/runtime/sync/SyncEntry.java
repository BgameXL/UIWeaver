package dev.uiweaver.runtime.sync;

import dev.uiweaver.api.sync.SyncType;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public record SyncEntry(String key, SyncType type, Object value) {

    public int     asInt()     { return (Integer) value; }
    public long    asLong()    { return (Long) value; }
    public boolean asBoolean() { return (Boolean) value; }
    public float   asFloat()   { return (Float) value; }
    public String  asString()  { return (String) value; }

    @SuppressWarnings("unchecked")
    public List<String> asStringList() { return (List<String>) value; }

    @SuppressWarnings("unchecked")
    public List<CompoundTag> asNbtList() { return (List<CompoundTag>) value; }
}