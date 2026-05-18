package dev.uiweaver.forge.client.render.widget;

import dev.uiweaver.api.component.CheckboxComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.theme.UITheme;
import dev.uiweaver.forge.client.render.ForgeWidgetRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class CheckboxRenderer implements ForgeWidgetRenderer<CheckboxComponent> {

    private static final int CHECK_COLOR  = 0xFF55FF55;
    private static final int BOX_BG       = 0xFF222222;

    @Override
    public void render(GuiGraphics graphics, CheckboxComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (layer != RenderLayer.FOREGROUND) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        int box = component.getBoxSize();
        int boxY = b.y() + (b.height() - box) / 2;

        graphics.fill(b.x(), boxY, b.x() + box, boxY + box, BOX_BG);
        graphics.renderOutline(b.x(), boxY, box, box, theme.getBorderColor());

        if (component.isChecked()) {
            int pad = 2;
            graphics.fill(b.x() + pad, boxY + pad, b.x() + box - pad, boxY + box - pad, CHECK_COLOR);
        }

        Font font = Minecraft.getInstance().font;
        int textX = b.x() + box + 4;
        int textY = b.y() + (b.height() - font.lineHeight) / 2;
        int color = component.isEnabled() ? theme.getTextColor() : theme.getTextColorDisabled();
        graphics.drawString(font, component.getLabel(), textX, textY, color, false);
    }
}