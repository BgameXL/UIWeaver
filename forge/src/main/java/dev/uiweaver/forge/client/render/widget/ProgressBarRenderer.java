package dev.uiweaver.forge.client.render.widget;

import dev.uiweaver.api.component.ProgressBarComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.render.WidgetRenderer;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.gui.GuiGraphics;

public class ProgressBarRenderer implements WidgetRenderer<ProgressBarComponent> {

    private static final int FILL_COLOR = 0xFF55AA55;

    @Override
    public void render(GuiGraphics graphics, ProgressBarComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (layer != RenderLayer.BACKGROUND) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        graphics.fill(b.x(), b.y(), b.right(), b.bottom(), theme.getFluidBarBackgroundColor());
        graphics.renderOutline(b.x(), b.y(), b.width(), b.height(), theme.getBorderColor());

        float fill = component.getFillFraction();
        if (fill <= 0f) return;

        int inner_x1 = b.x() + 1;
        int inner_y1 = b.y() + 1;
        int inner_x2 = b.right() - 1;
        int inner_y2 = b.bottom() - 1;
        int innerW = inner_x2 - inner_x1;
        int innerH = inner_y2 - inner_y1;

        int fx1 = inner_x1, fy1 = inner_y1, fx2 = inner_x2, fy2 = inner_y2;
        switch (component.getDirection()) {
            case LEFT_TO_RIGHT -> fx2 = inner_x1 + (int) (innerW * fill);
            case RIGHT_TO_LEFT -> fx1 = inner_x2 - (int) (innerW * fill);
            case TOP_TO_BOTTOM -> fy2 = inner_y1 + (int) (innerH * fill);
            case BOTTOM_TO_TOP -> fy1 = inner_y2 - (int) (innerH * fill);
        }

        graphics.fill(fx1, fy1, fx2, fy2, FILL_COLOR);
    }
}