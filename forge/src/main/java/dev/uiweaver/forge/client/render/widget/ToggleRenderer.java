package dev.uiweaver.forge.client.render.widget;

import dev.uiweaver.api.component.ToggleComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.theme.UITheme;
import dev.uiweaver.forge.client.render.ForgeWidgetRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class ToggleRenderer implements ForgeWidgetRenderer<ToggleComponent> {

    private static final int ON_COLOR  = 0xFF33BB55;
    private static final int OFF_COLOR = 0xFF773333;
    private static final int KNOB      = 0xFFDDDDDD;

    @Override
    public void render(GuiGraphics graphics, ToggleComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (layer != RenderLayer.FOREGROUND) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        boolean on  = component.isOn();
        int trackBg = on ? ON_COLOR : OFF_COLOR;
        int knobSize = b.height() - 4;
        int knobX    = on ? b.right() - knobSize - 2 : b.x() + 2;
        int knobY    = b.y() + 2;

        graphics.fill(b.x(), b.y(), b.right(), b.bottom(), trackBg);
        graphics.renderOutline(b.x(), b.y(), b.width(), b.height(), theme.getBorderColor());

        graphics.fill(knobX, knobY, knobX + knobSize, knobY + knobSize, KNOB);

        Font font = Minecraft.getInstance().font;
        String text = component.getActiveLabel().getString();
        int textX = b.x() + (b.width() - font.width(text)) / 2;
        int textY = b.y() + (b.height() - font.lineHeight) / 2;
        graphics.drawString(font, text, textX, textY, 0xFF000000, false);
    }
}