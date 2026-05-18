package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;

public class PlayerInventoryBuilder extends ComponentBuilder<PlayerInventoryBuilder> {

    private static final int SLOT  = 18;
    private static final int COLS  = 9;
    private static final int ROWS  = 3;
    private static final int GAP   = 4; // gap between inventory and hotbar

    private boolean includeHotbar = true;

    public PlayerInventoryBuilder includeHotbar(boolean include) {
        this.includeHotbar = include;
        return this;
    }

    @Override
    public PlayerInventoryComponent build() {
        int w = COLS * SLOT;
        int h = ROWS * SLOT + (includeHotbar ? GAP + SLOT : 0);
        if (preferredSize == Size.WRAP) preferredSize = Size.fixed(w, h);
        return new PlayerInventoryComponent(id, visible, enabled, preferredSize, includeHotbar);
    }
}