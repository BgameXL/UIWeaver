package dev.uiweaver.api.action;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ActionContext {

    ServerPlayer player();

    String actionId();

    CompoundTag payload();

    <T extends BlockEntity> T blockEntity(Class<T> type);

    boolean hasBlockEntity();
}