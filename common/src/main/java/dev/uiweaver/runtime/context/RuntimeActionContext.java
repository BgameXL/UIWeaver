package dev.uiweaver.runtime.context;

import dev.uiweaver.api.action.ActionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RuntimeActionContext implements ActionContext {

    private final ServerPlayer player;
    private final BlockPos pos;
    private final String actionId;

    public RuntimeActionContext(ServerPlayer player, BlockPos pos, String actionId) {
        this.player = player;
        this.pos = pos;
        this.actionId = actionId;
    }

    @Override
    public ServerPlayer player() {
        return player;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> T blockEntity(Class<T> type) {
        if (pos == null) throw new IllegalStateException("No BlockPos in context");
        ServerLevel level = player.serverLevel();
        BlockEntity be = level.getBlockEntity(pos);
        if (be == null || !type.isInstance(be))
            throw new IllegalStateException("BlockEntity at " + pos + " is not " + type.getSimpleName());
        return (T) be;
    }

    @Override
    public boolean hasBlockEntity() {
        if (pos == null) return false;
        return player.serverLevel().getBlockEntity(pos) != null;
    }

    @Override
    public String actionId() {
        return actionId;
    }
}