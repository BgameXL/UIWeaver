package dev.uiweaver.api.spec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.entity.BlockEntity;

public class UIContextSpec {

    public static final UIContextSpec NONE = new UIContextSpec(ContextType.NONE, null, null, 8);

    public enum ContextType { NONE, BLOCK_ENTITY, ITEM }

    private final ContextType type;
    private final Class<? extends BlockEntity> blockEntityClass;
    private final InteractionHand hand;
    private final int maxDistance;

    private UIContextSpec(ContextType type, Class<? extends BlockEntity> blockEntityClass,
                          InteractionHand hand, int maxDistance) {
        this.type = type;
        this.blockEntityClass = blockEntityClass;
        this.hand = hand;
        this.maxDistance = maxDistance;
    }

    public static UIContextSpec forBlockEntity(Class<? extends BlockEntity> clazz) {
        return new UIContextSpec(ContextType.BLOCK_ENTITY, clazz, null, 8);
    }

    public static UIContextSpec forItem(InteractionHand hand) {
        return new UIContextSpec(ContextType.ITEM, null, hand, 0);
    }

    public UIContextSpec maxDistance(int blocks) {
        return new UIContextSpec(type, blockEntityClass, hand, blocks);
    }

    public ContextType getType() { return type; }
    public Class<? extends BlockEntity> getBlockEntityClass() { return blockEntityClass; }
    public InteractionHand getHand() { return hand; }
    public int getMaxDistance() { return maxDistance; }
}
