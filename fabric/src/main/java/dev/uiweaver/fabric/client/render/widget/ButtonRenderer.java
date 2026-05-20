package dev.uiweaver.fabric.client.render.widget;

import dev.uiweaver.api.component.ButtonComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.fabric.client.render.FabricWidgetRenderer;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class ButtonRenderer implements FabricWidgetRenderer<ButtonComponent> {

    @Override
    public void render(GuiGraphics graphics, ButtonComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (layer != RenderLayer.FOREGROUND) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        boolean hovered = b.contains(mouseX, mouseY) && component.isEnabled();
        int bgColor = hovered ? theme.getButtonHoverColor() : theme.getButtonColor();

        graphics.fill(b.x(), b.y(), b.right(), b.bottom(), bgColor);
        graphics.renderOutline(b.x(), b.y(), b.width(), b.height(), theme.getBorderColor());

        Font font = Minecraft.getInstance().font;
        String text = component.getLabel().getString();
        int textX = b.x() + (b.width() - font.width(text)) / 2;
        int textY = b.y() + (b.height() - font.lineHeight) / 2;
        int textColor = component.isEnabled() ? theme.getTextColor() : theme.getTextColorDisabled();

        graphics.drawString(font, text, textX, textY, textColor, false);
    }
}