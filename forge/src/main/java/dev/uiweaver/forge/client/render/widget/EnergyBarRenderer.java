package dev.uiweaver.forge.client.render.widget;

import dev.uiweaver.api.component.EnergyBarComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.theme.UITheme;
import dev.uiweaver.forge.client.render.ForgeWidgetRenderer;
import net.minecraft.client.gui.GuiGraphics;

public class EnergyBarRenderer implements ForgeWidgetRenderer<EnergyBarComponent> {

    @Override
    public void render(GuiGraphics graphics, EnergyBarComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (layer != RenderLayer.BACKGROUND) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        graphics.fill(b.x(), b.y(), b.right(), b.bottom(), theme.getFluidBarBackgroundColor());
        graphics.renderOutline(b.x(), b.y(), b.width(), b.height(), theme.getBorderColor());

        float fill = component.getFillFraction();
        if (fill > 0f) {
            int fillHeight = (int) ((b.height() - 2) * fill);
            int fillY = b.bottom() - 1 - fillHeight;
            graphics.fill(b.x() + 1, fillY, b.right() - 1, b.bottom() - 1, theme.getEnergyBarColor());
        }
    }
}