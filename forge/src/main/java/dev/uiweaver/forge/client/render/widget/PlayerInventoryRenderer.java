package dev.uiweaver.forge.client.render.widget;

import dev.uiweaver.api.component.PlayerInventoryComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.render.WidgetRenderer;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.gui.GuiGraphics;

public class PlayerInventoryRenderer implements WidgetRenderer<PlayerInventoryComponent> {

    private static final int SLOT_SIZE  = 18;
    private static final int COLS       = 9;
    private static final int ROWS       = 3;
    private static final int HOTBAR_GAP = 4;

    private static final int SLOT_BG     = 0xFF8B8B8B;
    private static final int SLOT_BORDER = 0xFF373737;

    @Override
    public void render(GuiGraphics graphics, PlayerInventoryComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (layer != RenderLayer.BACKGROUND) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        // main inventory — 3 rows x 9 cols
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                drawSlot(graphics, b.x() + col * SLOT_SIZE, b.y() + row * SLOT_SIZE);
            }
        }

        // hotbar — 1 row x 9 cols, below with gap
        if (component.isIncludeHotbar()) {
            int hotbarY = b.y() + ROWS * SLOT_SIZE + HOTBAR_GAP;
            for (int col = 0; col < COLS; col++) {
                drawSlot(graphics, b.x() + col * SLOT_SIZE, hotbarY);
            }
        }
    }

    private void drawSlot(GuiGraphics graphics, int x, int y) {
        graphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, SLOT_BORDER);
        graphics.fill(x + 1, y + 1, x + SLOT_SIZE - 1, y + SLOT_SIZE - 1, SLOT_BG);
    }
}