package dev.uiweaver.runtime.context;

import dev.uiweaver.api.context.UIContextPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ContextValidator {

    public enum Result {
        OK,
        TOO_FAR,
        BLOCK_ENTITY_MISSING,
        BLOCK_ENTITY_WRONG_TYPE,
        WRONG_DIMENSION,
        ITEM_MISSING
    }

    public static Result validate(ServerPlayer player, UIContextPayload payload) {
        if (payload instanceof UIContextPayload.BlockContext bc) return validateBlock(player, bc);
        if (payload instanceof UIContextPayload.ItemContext ic) return validateItem(player, ic);
        return Result.OK;
    }

    private static Result validateBlock(ServerPlayer player, UIContextPayload.BlockContext bc) {
        if (!player.serverLevel().dimension().equals(bc.dimension())) return Result.WRONG_DIMENSION;

        BlockPos pos = bc.pos();
        double distSqr = player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        int max = bc.maxDistance();
        if (distSqr > (double) max * max) return Result.TOO_FAR;

        BlockEntity be = player.serverLevel().getBlockEntity(pos);
        if (be == null) return Result.BLOCK_ENTITY_MISSING;
        if (!bc.blockEntityClass().isInstance(be)) return Result.BLOCK_ENTITY_WRONG_TYPE;

        return Result.OK;
    }

    private static Result validateItem(ServerPlayer player, UIContextPayload.ItemContext ic) {
        if (player.getItemInHand(ic.hand()).isEmpty()) return Result.ITEM_MISSING;
        return Result.OK;
    }
}