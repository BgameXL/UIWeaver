package dev.uiweaver.runtime.context;

import dev.uiweaver.api.spec.UIContextSpec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ContextValidator {

    public enum Result {
        OK,
        TOO_FAR,
        BLOCK_ENTITY_MISSING,
        BLOCK_ENTITY_WRONG_TYPE,
        ITEM_MISSING
    }

    public static Result validate(ServerPlayer player, BlockPos pos, UIContextSpec spec) {
        return switch (spec.getType()) {
            case NONE -> Result.OK;
            case BLOCK_ENTITY -> validateBlockEntity(player, pos, spec);
            case ITEM -> validateItem(player, spec);
        };
    }

    private static Result validateBlockEntity(ServerPlayer player, BlockPos pos, UIContextSpec spec) {
        if (pos == null) return Result.BLOCK_ENTITY_MISSING;

        int maxDist = spec.getMaxDistance();
        double distSqr = player.distanceToSqr(
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        if (distSqr > (double) maxDist * maxDist) return Result.TOO_FAR;

        BlockEntity be = player.serverLevel().getBlockEntity(pos);
        if (be == null) return Result.BLOCK_ENTITY_MISSING;
        if (!spec.getBlockEntityClass().isInstance(be)) return Result.BLOCK_ENTITY_WRONG_TYPE;

        return Result.OK;
    }

    private static Result validateItem(ServerPlayer player, UIContextSpec spec) {
        var hand = spec.getHand();
        if (hand == null) return Result.ITEM_MISSING;
        if (player.getItemInHand(hand).isEmpty()) return Result.ITEM_MISSING;
        return Result.OK;
    }
}