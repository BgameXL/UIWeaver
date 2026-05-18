package dev.uiweaver.runtime.network;

import dev.uiweaver.api.sync.SyncType;
import dev.uiweaver.runtime.sync.SyncEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;

public class SyncPacket {

    public static final String ID = "uiweaver:sync";

    private final String screenId;
    private final List<SyncEntry> entries;

    public SyncPacket(String screenId, List<SyncEntry> entries) {
        this.screenId = screenId;
        this.entries  = entries;
    }

    public static SyncPacket decode(FriendlyByteBuf buf) {
        String screenId = buf.readUtf();
        int count = buf.readVarInt();
        List<SyncEntry> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String key   = buf.readUtf();
            SyncType type = buf.readEnum(SyncType.class);
            Object value = switch (type) {
                case INT         -> buf.readInt();
                case LONG        -> buf.readLong();
                case BOOLEAN     -> buf.readBoolean();
                case FLOAT       -> buf.readFloat();
                case STRING      -> buf.readUtf();
                case STRING_LIST -> {
                    int len = buf.readVarInt();
                    List<String> list = new ArrayList<>(len);
                    for (int j = 0; j < len; j++) list.add(buf.readUtf());
                    yield List.copyOf(list);
                }
                case NBT_LIST -> {
                    int len = buf.readVarInt();
                    List<CompoundTag> list = new ArrayList<>(len);
                    for (int j = 0; j < len; j++) list.add(buf.readNbt());
                    yield List.copyOf(list);
                }
            };
            entries.add(new SyncEntry(key, type, value));
        }
        return new SyncPacket(screenId, entries);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(screenId);
        buf.writeVarInt(entries.size());
        for (SyncEntry e : entries) {
            buf.writeUtf(e.key());
            buf.writeEnum(e.type());
            switch (e.type()) {
                case INT     -> buf.writeInt(e.asInt());
                case LONG    -> buf.writeLong(e.asLong());
                case BOOLEAN -> buf.writeBoolean(e.asBoolean());
                case FLOAT   -> buf.writeFloat(e.asFloat());
                case STRING  -> buf.writeUtf(e.asString());
                case STRING_LIST -> {
                    List<String> list = e.asStringList();
                    buf.writeVarInt(list.size());
                    list.forEach(buf::writeUtf);
                }
                case NBT_LIST -> {
                    List<CompoundTag> list = e.asNbtList();
                    buf.writeVarInt(list.size());
                    list.forEach(buf::writeNbt);
                }
            }
        }
    }

    public String getScreenId()       { return screenId; }
    public List<SyncEntry> getEntries() { return entries; }
}