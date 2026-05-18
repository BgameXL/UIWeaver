package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;

public class PlayerInventoryComponent extends AbstractComponent implements Measurable {

    private static final int SLOT_SIZE  = 18;
    private static final int COLS       = 9;
    private static final int MAIN_ROWS  = 3;
    private static final int HOTBAR_GAP = 4;

    private final boolean includeHotbar;

    public PlayerInventoryComponent(String id, boolean visible, boolean enabled,
                                    Size preferredSize, boolean includeHotbar) {
        super(id, visible, enabled, preferredSize);
        this.includeHotbar = includeHotbar;
    }

    @Override public ComponentType getType() { return ComponentType.PLAYER_INVENTORY; }

    @Override
    public Size measure(int availableWidth, int availableHeight) {
        int w = COLS * SLOT_SIZE;
        int h = MAIN_ROWS * SLOT_SIZE;
        if (includeHotbar) h += HOTBAR_GAP + SLOT_SIZE;
        return Size.fixed(w, h);
    }

    public boolean isIncludeHotbar() { return includeHotbar; }
    public int getSlotCount()        { return includeHotbar ? 36 : 27; }
}