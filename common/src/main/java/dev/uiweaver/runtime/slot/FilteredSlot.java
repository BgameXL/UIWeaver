package dev.uiweaver.runtime.slot;

import dev.uiweaver.api.slot.SlotDescriptor;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FilteredSlot extends Slot {

    private final SlotDescriptor descriptor;

    public FilteredSlot(Container container, SlotDescriptor descriptor) {
        super(container, descriptor.getIndex(), descriptor.getX(), descriptor.getY());
        this.descriptor = descriptor;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (descriptor.isOutput() || descriptor.isLocked()) return false;
        if (descriptor.isGhost()) return true;
        return descriptor.getFilter().test(stack);
    }

    @Override
    public int getMaxStackSize() {
        return descriptor.getMaxStackSize();
    }

    @Override
    public boolean mayPickup(net.minecraft.world.entity.player.Player player) {
        return !descriptor.isLocked();
    }

    public SlotDescriptor getDescriptor() {
        return descriptor;
    }
}