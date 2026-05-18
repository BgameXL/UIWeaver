package dev.uiweaver.test;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestMachineBlockEntity extends BlockEntity {

    public static BlockEntityType<TestMachineBlockEntity> TYPE;

    private final EnergyStorage energy = new EnergyStorage(10000, 100, 100);
    private final LazyOptional<EnergyStorage> energyCap = LazyOptional.of(() -> energy);
    private final ItemStackHandler items = new ItemStackHandler(9);
    private boolean working = false;

    public TestMachineBlockEntity(BlockPos pos, BlockState state) {
        super(TYPE, pos, state);
        energy.receiveEnergy(10000, false);
    }

    public void tick() {
        if (working && energy.getEnergyStored() >= 10) {
            energy.extractEnergy(10, false);
            setChanged();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("energy")) energy.deserializeNBT(tag.get("energy"));
        if (tag.contains("items")) items.deserializeNBT(tag.getCompound("items"));
        working = tag.getBoolean("working");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("energy", energy.serializeNBT());
        tag.put("items", items.serializeNBT());
        tag.putBoolean("working", working);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) return energyCap.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyCap.invalidate();
    }

    public void start()  { working = true;  setChanged(); }
    public void stop()   { working = false; setChanged(); }

    public long getEnergy()    { return energy.getEnergyStored(); }
    public long getMaxEnergy() { return energy.getMaxEnergyStored(); }
    public boolean isWorking() { return working; }
}