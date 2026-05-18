package dev.uiweaver.runtime.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class ActionPacket {

    public static final String ID = "uiweaver:action";

    private final String screenId;
    private final String actionId;
    private final BlockPos pos;

    public ActionPacket(String screenId, String actionId, BlockPos pos) {
        this.screenId = screenId;
        this.actionId = actionId;
        this.pos = pos;
    }

    public static ActionPacket decode(FriendlyByteBuf buf) {
        String screenId = buf.readUtf();
        String actionId = buf.readUtf();
        boolean hasPos = buf.readBoolean();
        BlockPos pos = hasPos ? buf.readBlockPos() : null;
        return new ActionPacket(screenId, actionId, pos);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(screenId);
        buf.writeUtf(actionId);
        buf.writeBoolean(pos != null);
        if (pos != null) buf.writeBlockPos(pos);
    }

    public String getScreenId() { return screenId; }
    public String getActionId() { return actionId; }
    public BlockPos getPos() { return pos; }
}