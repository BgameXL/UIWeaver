package dev.uiweaver.fabric.client.render.widget;

import dev.uiweaver.api.component.LabelComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.render.WidgetRenderer;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class LabelRenderer implements WidgetRenderer<LabelComponent> {

    @Override
    public void render(GuiGraphics graphics, LabelComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (layer != RenderLayer.FOREGROUND) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        Font font = Minecraft.getInstance().font;
        String text = font.plainSubstrByWidth(component.getText().getString(),
                component.isEllipsis() ? component.getMaxWidth() : Integer.MAX_VALUE);

        graphics.drawString(font, text, b.x(), b.y(), theme.getTextColor(), false);
    }
}