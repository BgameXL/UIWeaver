package dev.uiweaver.fabric.client.render.widget;

import dev.uiweaver.api.component.TextInputComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.render.WidgetRenderer;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class TextInputRenderer implements WidgetRenderer<TextInputComponent> {

    private static final int PLACEHOLDER_COLOR = 0xFF666666;
    private static final int CURSOR_COLOR = 0xFFCCCCCC;
    private static final int PADDING = 3;

    @Override
    public void render(GuiGraphics graphics, TextInputComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (layer != RenderLayer.FOREGROUND) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        graphics.fill(b.x(), b.y(), b.right(), b.bottom(), theme.getFluidBarBackgroundColor());
        graphics.renderOutline(b.x(), b.y(), b.width(), b.height(), theme.getBorderColor());

        Font font = Minecraft.getInstance().font;
        int textX = b.x() + PADDING;
        int textY = b.y() + (b.height() - font.lineHeight) / 2;

        String value = component.getValue();
        if (value.isEmpty()) {
            graphics.drawString(font, component.getPlaceholder(), textX, textY, PLACEHOLDER_COLOR, false);
        } else {
            graphics.drawString(font, value, textX, textY, theme.getTextColor(), false);

            // simple end cursor
            int cursorX = textX + font.width(value);
            if (cursorX < b.right() - PADDING && (System.currentTimeMillis() / 500) % 2 == 0) {
                graphics.fill(cursorX, textY, cursorX + 1, textY + font.lineHeight, CURSOR_COLOR);
            }
        }
    }
}