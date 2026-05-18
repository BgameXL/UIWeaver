package dev.uiweaver.forge.client.render.widget;

import dev.uiweaver.api.component.TextInputComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.input.FocusManager;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.theme.UITheme;
import dev.uiweaver.forge.client.render.ForgeWidgetRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class TextInputRenderer implements ForgeWidgetRenderer<TextInputComponent> {

    private static final int PLACEHOLDER_COLOR = 0xFF666666;
    private static final int CURSOR_COLOR      = 0xFFCCCCCC;
    private static final int SELECTION_COLOR   = 0x663399FF;
    private static final int FOCUSED_BORDER    = 0xFF7EAAFF;
    private static final int PADDING           = 3;

    @Override
    public void render(GuiGraphics graphics, TextInputComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (layer != RenderLayer.FOREGROUND) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        boolean focused = FocusManager.isFocused(component);

        graphics.fill(b.x(), b.y(), b.right(), b.bottom(), theme.getFluidBarBackgroundColor());
        graphics.renderOutline(b.x(), b.y(), b.width(), b.height(),
                focused ? FOCUSED_BORDER : theme.getBorderColor());

        Font font = Minecraft.getInstance().font;
        int textX    = b.x() + PADDING;
        int textY    = b.y() + (b.height() - font.lineHeight) / 2;
        int maxTextW = b.width() - PADDING * 2;

        String value = component.getValue();

        if (value.isEmpty() && !focused) {
            graphics.drawString(font,
                    font.plainSubstrByWidth(component.getPlaceholder(), maxTextW),
                    textX, textY, PLACEHOLDER_COLOR, false);
            return;
        }

        int cursorPos   = component.getCursorPos();
        int cursorPixel = font.width(value.substring(0, cursorPos));
        int scrollX     = Math.max(0, cursorPixel - maxTextW + 2);

        // Selection
        if (component.hasSelection()) {
            int selStart = component.getSelectionStart();
            int lo  = Math.min(cursorPos, selStart);
            int hi  = Math.max(cursorPos, selStart);
            int sx1 = clamp(textX + font.width(value.substring(0, lo)) - scrollX, textX, b.right() - PADDING);
            int sx2 = clamp(textX + font.width(value.substring(0, hi)) - scrollX, textX, b.right() - PADDING);
            if (sx2 > sx1) graphics.fill(sx1, textY - 1, sx2, textY + font.lineHeight + 1, SELECTION_COLOR);
        }

        // Text
        int charOffset = scrollX > 0 ? Math.min(scrollX / 6, value.length()) : 0;
        graphics.drawString(font,
                font.plainSubstrByWidth(value.substring(charOffset), maxTextW),
                textX, textY, theme.getTextColor(), false);

        // Cursor blink
        if (focused && (System.currentTimeMillis() / 500) % 2 == 0) {
            int cx = textX + cursorPixel - scrollX;
            if (cx >= textX && cx <= b.right() - PADDING) {
                graphics.fill(cx, textY - 1, cx + 1, textY + font.lineHeight + 1, CURSOR_COLOR);
            }
        }
    }

    private static int clamp(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }
}