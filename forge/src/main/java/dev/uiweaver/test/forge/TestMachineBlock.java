package dev.uiweaver.test.forge;

import dev.uiweaver.api.context.UIContextPayload;
import dev.uiweaver.forge.registry.UIMenuRegistry;
import dev.uiweaver.test.TestMachineScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TestMachineBlock extends BaseEntityBlock {

    public TestMachineBlock() {
        super(BlockBehaviour.Properties.of().strength(3.5f).requiresCorrectToolForDrops());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof TestMachineBlockEntity machine)) return InteractionResult.PASS;
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.PASS;

        UIContextPayload context = UIContextPayload.forBlock(
                level.dimension(), pos, TestMachineBlockEntity.class, 8);

        UIMenuRegistry.open(serverPlayer,
                new TestMachineScreen(
                        machine::getEnergy,
                        machine::getMaxEnergy,
                        machine::isWorking,
                        machine::asContainer,
                        ctx -> ctx.blockEntity(TestMachineBlockEntity.class).start(),
                        ctx -> ctx.blockEntity(TestMachineBlockEntity.class).stop(),
                        context
                ),
                TestMachineScreen.ID,
                context
        );
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TestMachineBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                            BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, p, blockState, be) -> {
            if (be instanceof TestMachineBlockEntity machine) machine.tick();
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }
}