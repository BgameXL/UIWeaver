package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;

public class PlayerInventoryComponent extends AbstractComponent {

    private final boolean includeHotbar;

    public PlayerInventoryComponent(String id, boolean visible, boolean enabled,
                                    Size preferredSize, boolean includeHotbar) {
        super(id, visible, enabled, preferredSize);
        this.includeHotbar = includeHotbar;
    }

    @Override public ComponentType getType() { return ComponentType.PLAYER_INVENTORY; }

    public boolean isIncludeHotbar() { return includeHotbar; }

    // 9 cols x 3 rows = 27 slots, +9 hotbar = 36 total
    public int getSlotCount() { return includeHotbar ? 36 : 27; }
}