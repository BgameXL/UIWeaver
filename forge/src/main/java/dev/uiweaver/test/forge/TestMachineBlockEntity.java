package dev.uiweaver.test.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestMachineBlockEntity extends BlockEntity {

    public static BlockEntityType<TestMachineBlockEntity> TYPE;

    private final EnergyStorage energy = new EnergyStorage(10000, 100, 100);
    private final LazyOptional<EnergyStorage> energyCap = LazyOptional.of(() -> energy);
    private final ItemStackHandler items = new ItemStackHandler(9);
    private final LazyOptional<IItemHandler> itemCap = LazyOptional.of(() -> items);
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
        if (tag.contains("items"))  items.deserializeNBT(tag.getCompound("items"));
        working = tag.getBoolean("working");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("energy", energy.serializeNBT());
        tag.put("items",  items.serializeNBT());
        tag.putBoolean("working", working);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY)       return energyCap.cast();
        if (cap == ForgeCapabilities.ITEM_HANDLER) return itemCap.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyCap.invalidate();
        itemCap.invalidate();
    }

    public Container asContainer() {
        return new Container() {
            @Override
            public int getContainerSize() {
                return items.getSlots();
            }

            @Override
            public boolean isEmpty() {
                for (int i = 0; i < items.getSlots(); i++) {
                    if (!items.getStackInSlot(i).isEmpty()) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public ItemStack getItem(int slot) {
                return items.getStackInSlot(slot);
            }

            @Override
            public ItemStack removeItem(int slot, int amount) {
                ItemStack stack = items.getStackInSlot(slot);

                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }

                ItemStack extracted = stack.split(amount);

                if (stack.isEmpty()) {
                    items.setStackInSlot(slot, ItemStack.EMPTY);
                } else {
                    items.setStackInSlot(slot, stack);
                }

                setChanged();
                return extracted;
            }

            @Override
            public ItemStack removeItemNoUpdate(int slot) {
                ItemStack stack = items.getStackInSlot(slot);
                items.setStackInSlot(slot, ItemStack.EMPTY);
                setChanged();
                return stack;
            }

            @Override
            public void setItem(int slot, ItemStack stack) {
                items.setStackInSlot(slot, stack);
                setChanged();
            }

            @Override
            public void setChanged() {
                TestMachineBlockEntity.this.setChanged();
            }

            @Override
            public boolean stillValid(net.minecraft.world.entity.player.Player player) {
                return !isRemoved();
            }

            @Override
            public void clearContent() {
                for (int i = 0; i < items.getSlots(); i++) {
                    items.setStackInSlot(i, ItemStack.EMPTY);
                }

                setChanged();
            }
        };
    }

    public void start()  { working = true;  setChanged(); }
    public void stop()   { working = false; setChanged(); }

    public long getEnergy()    { return energy.getEnergyStored(); }
    public long getMaxEnergy() { return energy.getMaxEnergyStored(); }
    public boolean isWorking() { return working; }
}