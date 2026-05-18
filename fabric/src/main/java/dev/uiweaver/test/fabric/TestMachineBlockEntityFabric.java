package dev.uiweaver.test.fabric;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TestMachineBlockEntityFabric extends BlockEntity {

    public static BlockEntityType<TestMachineBlockEntityFabric> TYPE;

    private long energy = 10000;
    private final long maxEnergy = 10000;
    private boolean working = false;

    public TestMachineBlockEntityFabric(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
    }

    public void tick() {
        if (working && energy >= 10) {
            energy -= 10;
            setChanged();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        energy  = tag.getLong("energy");
        working = tag.getBoolean("working");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putLong("energy", energy);
        tag.putBoolean("working", working);
    }

    public void start()  { working = true;  setChanged(); }
    public void stop()   { working = false; setChanged(); }

    public long getEnergy()    { return energy; }
    public long getMaxEnergy() { return maxEnergy; }
    public boolean isWorking() { return working; }
}