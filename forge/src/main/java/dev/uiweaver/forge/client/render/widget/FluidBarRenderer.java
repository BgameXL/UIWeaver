package dev.uiweaver.forge.client.render.widget;

import dev.uiweaver.api.component.FluidBarComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.forge.client.render.ForgeWidgetRenderer;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class FluidBarRenderer implements ForgeWidgetRenderer<FluidBarComponent> {

    private static final int DEFAULT_FLUID_COLOR = 0xFF3355FF;

    @Override
    public void render(GuiGraphics graphics, FluidBarComponent component, UIViewModel viewModel,
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
                graphics.fill(b.x() + 1, fillY, b.right() - 1, b.bottom() - 1, DEFAULT_FLUID_COLOR);
            }
        }

        if (layer == RenderLayer.TOOLTIP && b.contains(mouseX, mouseY)) {
            String fluidName = component.getFluidId().isEmpty() ? "Empty" : component.getFluidId();
            Component line1 = Component.literal(fluidName);
            Component line2 = Component.literal(component.getAmount() + " / " + component.getCapacity() + " mB");
            List<FormattedCharSequence> lines = List.of(
                    line1.getVisualOrderText(),
                    line2.getVisualOrderText()
            );
            graphics.renderTooltip(Minecraft.getInstance().font, lines, mouseX, mouseY);
        }
    }
}