package dev.uiweaver.api.slot;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class SlotDescriptor {

    public enum Role { INPUT, OUTPUT, GHOST, UPGRADE, FILTER, GENERAL }

    private final int index;
    private final int x;
    private final int y;
    private final Role role;
    private final Predicate<ItemStack> filter;
    private final int maxStackSize;
    private final boolean locked;

    private SlotDescriptor(Builder builder) {
        this.index = builder.index;
        this.x = builder.x;
        this.y = builder.y;
        this.role = builder.role;
        this.filter = builder.filter;
        this.maxStackSize = builder.maxStackSize;
        this.locked = builder.locked;
    }

    public int getIndex() { return index; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Role getRole() { return role; }
    public Predicate<ItemStack> getFilter() { return filter; }
    public int getMaxStackSize() { return maxStackSize; }
    public boolean isLocked() { return locked; }
    public boolean isGhost() { return role == Role.GHOST; }
    public boolean isOutput() { return role == Role.OUTPUT; }

    public static Builder builder(int index, int x, int y) {
        return new Builder(index, x, y);
    }

    public static class Builder {
        private final int index, x, y;
        private Role role = Role.GENERAL;
        private Predicate<ItemStack> filter = stack -> true;
        private int maxStackSize = 64;
        private boolean locked = false;

        private Builder(int index, int x, int y) {
            this.index = index;
            this.x = x;
            this.y = y;
        }

        public Builder role(Role role) { this.role = role; return this; }
        public Builder filter(Predicate<ItemStack> filter) { this.filter = filter; return this; }
        public Builder maxStackSize(int size) { this.maxStackSize = size; return this; }
        public Builder locked() { this.locked = true; return this; }
        public Builder ghost() { return role(Role.GHOST); }
        public Builder outputOnly() { return role(Role.OUTPUT).locked(); }

        public SlotDescriptor build() { return new SlotDescriptor(this); }
    }
}
