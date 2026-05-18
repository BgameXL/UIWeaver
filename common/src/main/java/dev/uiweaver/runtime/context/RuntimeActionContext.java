package dev.uiweaver.runtime.context;

import dev.uiweaver.api.action.ActionContext;
import dev.uiweaver.api.context.UIContextPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RuntimeActionContext implements ActionContext {

    private final ServerPlayer player;
    private final UIContextPayload payload;
    private final String actionId;
    private final CompoundTag data;

    public RuntimeActionContext(ServerPlayer player, UIContextPayload payload,
                                String actionId, CompoundTag data) {
        this.player   = player;
        this.payload  = payload;
        this.actionId = actionId;
        this.data     = data != null ? data : new CompoundTag();
    }

    @Override public ServerPlayer player()    { return player; }
    @Override public String actionId()        { return actionId; }
    @Override public CompoundTag payload()    { return data; }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> T blockEntity(Class<T> type) {
        if (!(payload instanceof UIContextPayload.BlockContext bc))
            throw new IllegalStateException("Context is not a BlockContext");
        BlockEntity be = player.serverLevel().getBlockEntity(bc.pos());
        if (be == null || !type.isInstance(be))
            throw new IllegalStateException("BlockEntity at " + bc.pos() + " is not " + type.getSimpleName());
        return (T) be;
    }

    @Override
    public boolean hasBlockEntity() {
        if (!(payload instanceof UIContextPayload.BlockContext bc)) return false;
        return player.serverLevel().getBlockEntity(bc.pos()) != null;
    }
}