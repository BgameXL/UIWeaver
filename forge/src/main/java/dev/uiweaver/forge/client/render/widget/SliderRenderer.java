package dev.uiweaver.forge.client.render.widget;

import dev.uiweaver.api.component.SliderComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.theme.UITheme;
import dev.uiweaver.forge.client.render.ForgeWidgetRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class SliderRenderer implements ForgeWidgetRenderer<SliderComponent> {

    private static final int TRACK_COLOR = 0xFF333333;
    private static final int FILL_COLOR  = 0xFF4477AA;
    private static final int THUMB_COLOR = 0xFFAAAAAA;
    private static final int THUMB_HOVER = 0xFFDDDDDD;

    @Override
    public void render(GuiGraphics graphics, SliderComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (layer != RenderLayer.FOREGROUND) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        int thumbSize = b.height();
        int thumbCX   = b.x() + component.getThumbX(b.width());
        int trackY    = b.y() + b.height() / 2 - 1;

        graphics.fill(b.x(), trackY, b.right(), trackY + 2, TRACK_COLOR);
        graphics.fill(b.x(), trackY, thumbCX, trackY + 2, FILL_COLOR);

        boolean hovered = b.contains(mouseX, mouseY) || component.isDragging();
        int thumbColor  = hovered ? THUMB_HOVER : THUMB_COLOR;
        int tx = thumbCX - thumbSize / 2;
        graphics.fill(tx, b.y(), tx + thumbSize, b.bottom(), thumbColor);
        graphics.renderOutline(tx, b.y(), thumbSize, thumbSize, theme.getBorderColor());

        Font font = Minecraft.getInstance().font;
        String label = String.valueOf(component.getValue());
        int textX = b.x() + (b.width() - font.width(label)) / 2;
        int textY = b.y() + (b.height() - font.lineHeight) / 2;
        graphics.drawString(font, label, textX, textY, theme.getTextColor(), false);
    }
}