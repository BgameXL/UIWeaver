package dev.uiweaver.example;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ExampleMachineBlockEntity extends BlockEntity {

    private long energy = 0;
    private long maxEnergy = 10000;
    private boolean working = false;

    public ExampleMachineBlockEntity(BlockEntityType<?> type, BlockPos pos,
                                     net.minecraft.world.level.block.state.BlockState state) {
        super(type, pos, state);
    }

    public long getEnergy() { return energy; }
    public long getMaxEnergy() { return maxEnergy; }
    public boolean isWorking() { return working; }

    public void start() { working = true; }
    public void stop() { working = false; }
}