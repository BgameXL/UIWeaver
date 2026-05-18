package dev.uiweaver.fabric.client.render.widget;

import dev.uiweaver.api.component.SlotGridComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.slot.SlotDescriptor;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.render.WidgetRenderer;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.gui.GuiGraphics;

public class SlotGridRenderer implements WidgetRenderer<SlotGridComponent> {

    private static final int SLOT_BG = 0xFF8B8B8B;
    private static final int SLOT_BORDER = 0xFF373737;
    private static final int SLOT_SIZE = 18;

    @Override
    public void render(GuiGraphics graphics, SlotGridComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (layer != RenderLayer.BACKGROUND) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        for (SlotDescriptor slot : component.getSlots()) {
            int x = b.x() + slot.getX();
            int y = b.y() + slot.getY();
            graphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, SLOT_BORDER);
            graphics.fill(x + 1, y + 1, x + SLOT_SIZE - 1, y + SLOT_SIZE - 1, SLOT_BG);
        }
    }
}