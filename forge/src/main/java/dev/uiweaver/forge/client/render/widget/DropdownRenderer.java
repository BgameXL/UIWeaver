package dev.uiweaver.forge.client.render.widget;

import dev.uiweaver.api.component.DropdownComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.popup.PopupManager;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.theme.UITheme;
import dev.uiweaver.forge.client.render.ForgeWidgetRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class DropdownRenderer implements ForgeWidgetRenderer<DropdownComponent> {

    private static final int PAD_H = 6;
    private static final int PAD_V = 4;

    @Override
    public void render(GuiGraphics graphics, DropdownComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (layer != RenderLayer.FOREGROUND) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        boolean hovered = b.contains(mouseX, mouseY);
        int bg = hovered ? theme.getButtonHoverColor() : theme.getButtonColor();

        graphics.fill(b.x(), b.y(), b.right(), b.bottom(), bg);
        graphics.renderOutline(b.x(), b.y(), b.width(), b.height(), theme.getBorderColor());

        Font font = Minecraft.getInstance().font;

        String text = component.getSelectedOption().getString();
        int textY = b.y() + (b.height() - font.lineHeight) / 2;
        graphics.drawString(font, text, b.x() + PAD_H, textY, theme.getTextColor(), false);

        int arrowW = component.getArrowWidth();
        int arrowX = b.right() - arrowW;
        graphics.fill(arrowX, b.y(), arrowX + 1, b.bottom(), theme.getBorderColor());
        graphics.drawString(font, "▼", arrowX + (arrowW - font.width("▼")) / 2, textY,
                theme.getTextColorDisabled(), false);
    }
}