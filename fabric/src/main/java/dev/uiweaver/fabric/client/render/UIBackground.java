package dev.uiweaver.fabric.client.render;

import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class UIBackground {

    private static final ResourceLocation DEFAULT_TEXTURE =
            new ResourceLocation("uiweaver", "textures/gui/panel.png");

    private static final int DEFAULT_TEXTURE_W = 256;
    private static final int DEFAULT_TEXTURE_H = 256;

    public static void render(GuiGraphics graphics, UITheme theme,
                               int x, int y, int width, int height) {
        ResourceLocation texture = theme.getPanelTexture();

        if (texture != null) {
            renderTextured(graphics, texture, x, y, width, height);
        } else {
            renderSolid(graphics, theme, x, y, width, height);
        }
    }

    private static void renderTextured(GuiGraphics graphics, ResourceLocation texture,
                                        int x, int y, int width, int height) {
        graphics.blit(texture, x, y, 0, 0, width, height, DEFAULT_TEXTURE_W, DEFAULT_TEXTURE_H);
    }

    private static void renderSolid(GuiGraphics graphics, UITheme theme,
                                     int x, int y, int width, int height) {
        // outer shadow
        graphics.fill(x + 2, y + 2, x + width + 2, y + height + 2, 0x55000000);
        // main background
        graphics.fill(x, y, x + width, y + height, theme.getBackgroundColor());
        // border
        graphics.renderOutline(x, y, width, height, theme.getBorderColor());
        // inner highlight top-left
        graphics.fill(x + 1, y + 1, x + width - 1, y + 2, 0x22FFFFFF);
        graphics.fill(x + 1, y + 1, x + 2, y + height - 1, 0x22FFFFFF);
    }
}