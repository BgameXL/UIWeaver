package dev.uiweaver.runtime.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class ActionPacket {

    public static final String ID = "uiweaver:action";

    private final int sessionId;
    private final String screenId;
    private final String actionId;
    private final CompoundTag payload;

    public ActionPacket(int sessionId, String screenId, String actionId, CompoundTag payload) {
        this.sessionId = sessionId;
        this.screenId  = screenId;
        this.actionId  = actionId;
        this.payload   = payload != null ? payload : new CompoundTag();
    }

    public ActionPacket(int sessionId, String screenId, String actionId) {
        this(sessionId, screenId, actionId, new CompoundTag());
    }

    public static ActionPacket decode(FriendlyByteBuf buf) {
        int sessionId   = buf.readInt();
        String screenId = buf.readUtf();
        String actionId = buf.readUtf();
        CompoundTag payload = buf.readNbt();
        return new ActionPacket(sessionId, screenId, actionId, payload);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(sessionId);
        buf.writeUtf(screenId);
        buf.writeUtf(actionId);
        buf.writeNbt(payload);
    }

    public int getSessionId()       { return sessionId; }
    public String getScreenId()     { return screenId; }
    public String getActionId()     { return actionId; }
    public CompoundTag getPayload() { return payload; }
}