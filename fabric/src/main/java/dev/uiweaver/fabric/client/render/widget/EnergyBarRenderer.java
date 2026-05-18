package dev.uiweaver.fabric.client.render.widget;

import dev.uiweaver.api.component.EnergyBarComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.render.WidgetRenderer;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class EnergyBarRenderer implements WidgetRenderer<EnergyBarComponent> {

    @Override
    public void render(GuiGraphics graphics, EnergyBarComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        Bounds b = component.getBounds();
        if (b == null) return;

        if (layer == RenderLayer.BACKGROUND) {
            graphics.fill(b.x(), b.y(), b.right(), b.bottom(), theme.getFluidBarBackgroundColor());
            graphics.renderOutline(b.x(), b.y(), b.width(), b.height(), theme.getBorderColor());

            float fill = component.getFillFraction();
            if (fill > 0f) {
                int fillHeight = (int) ((b.height() - 2) * fill);
                int fillY = b.bottom() - 1 - fillHeight;
                graphics.fill(b.x() + 1, fillY, b.right() - 1, b.bottom() - 1, theme.getEnergyBarColor());
            }
        }

        if (layer == RenderLayer.TOOLTIP && b.contains(mouseX, mouseY)) {
            Component text = Component.literal(component.getEnergy() + " / " + component.getMaxEnergy() + " FE");
            List<FormattedCharSequence> lines = List.of(text.getVisualOrderText());
            graphics.renderTooltip(Minecraft.getInstance().font, lines, mouseX, mouseY);
        }
    }
}