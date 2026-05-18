package dev.uiweaver.forge.client.popup;

import dev.uiweaver.client.popup.ContextMenuItem;
import dev.uiweaver.client.popup.PopupManager;
import dev.uiweaver.client.popup.PopupMenu;
import dev.uiweaver.client.theme.ThemeRegistry;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class PopupRenderer {

    public static final int ITEM_H     = 12;
    public static final int SEP_H      = 5;
    public static final int PAD_H      = 6;
    public static final int PAD_V      = 3;
    public static final int MIN_W      = 80;

    public static void render(GuiGraphics graphics, int screenW, int screenH, int mouseX, int mouseY) {
        PopupMenu menu = PopupManager.getActive();
        if (menu == null) return;

        Font font     = Minecraft.getInstance().font;
        UITheme theme = ThemeRegistry.getActive();
        List<ContextMenuItem> items = menu.getItems();

        // Compute dimensions
        int w = MIN_W;
        for (ContextMenuItem item : items) {
            if (!item.isSeparator()) w = Math.max(w, font.width(item.getLabel()) + PAD_H * 2);
        }

        int h = PAD_V;
        for (ContextMenuItem item : items) h += item.isSeparator() ? SEP_H : ITEM_H;
        h += PAD_V;

        // Position — flip left if overflowing right, flip up if overflowing bottom
        int x = menu.getAnchorX();
        int y = menu.getAnchorY();
        if (x + w > screenW) x = screenW - w;
        if (y + h > screenH) y = Math.max(0, screenH - h);

        graphics.fill(x, y, x + w, y + h, theme.getBackgroundColor());
        graphics.renderOutline(x, y, w, h, theme.getBorderColor());

        int iy = y + PAD_V;
        for (ContextMenuItem item : items) {
            if (item.isSeparator()) {
                int sy = iy + SEP_H / 2;
                graphics.fill(x + PAD_H, sy, x + w - PAD_H, sy + 1, theme.getBorderColor());
                iy += SEP_H;
                continue;
            }

            boolean hovered = mouseX >= x && mouseX < x + w && mouseY >= iy && mouseY < iy + ITEM_H;
            if (hovered && item.isEnabled()) {
                graphics.fill(x + 1, iy, x + w - 1, iy + ITEM_H, theme.getButtonHoverColor());
            }

            int color = item.isEnabled() ? theme.getTextColor() : theme.getTextColorDisabled();
            graphics.drawString(font, item.getLabel(), x + PAD_H, iy + (ITEM_H - font.lineHeight) / 2, color, false);
            iy += ITEM_H;
        }
    }

    public static ContextMenuItem itemAt(PopupMenu menu, int mouseX, int mouseY, int screenW, int screenH) {
        Font font = Minecraft.getInstance().font;
        List<ContextMenuItem> items = menu.getItems();

        int w = MIN_W;
        for (ContextMenuItem item : items) {
            if (!item.isSeparator()) w = Math.max(w, font.width(item.getLabel()) + PAD_H * 2);
        }

        int h = PAD_V;
        for (ContextMenuItem item : items) h += item.isSeparator() ? SEP_H : ITEM_H;
        h += PAD_V;

        int x = menu.getAnchorX();
        int y = menu.getAnchorY();
        if (x + w > screenW) x = screenW - w;
        if (y + h > screenH) y = Math.max(0, screenH - h);

        if (mouseX < x || mouseX >= x + w) return null;

        int iy = y + PAD_V;
        for (ContextMenuItem item : items) {
            int ih = item.isSeparator() ? SEP_H : ITEM_H;
            if (!item.isSeparator() && mouseY >= iy && mouseY < iy + ih) return item;
            iy += ih;
        }
        return null;
    }
}