package dev.uiweaver.fabric.client.render.widget;

import dev.uiweaver.api.component.ScrollPanelComponent;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.render.WidgetRenderer;
import dev.uiweaver.client.render.WidgetRendererRegistry;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class ScrollPanelRenderer implements WidgetRenderer<ScrollPanelComponent> {

    private static final int SCROLLBAR_WIDTH = 4;
    private static final int SCROLLBAR_COLOR = 0xFF555555;
    private static final int SCROLLBAR_THUMB = 0xFF888888;

    @Override
    public void render(GuiGraphics graphics, ScrollPanelComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        Bounds b = component.getBounds();
        if (b == null) return;

        if (layer == RenderLayer.BACKGROUND) {
            graphics.fill(b.x(), b.y(), b.right(), b.bottom(), theme.getFluidBarBackgroundColor());

            if (component.canScroll()) {
                renderScrollbar(graphics, component, b);
            }
        }

        if (layer == RenderLayer.FOREGROUND) {
            renderChildren(graphics, component, viewModel, theme, mouseX, mouseY, b);
        }
    }

    private void renderChildren(GuiGraphics graphics, ScrollPanelComponent component,
                                UIViewModel viewModel, UITheme theme,
                                int mouseX, int mouseY, Bounds b) {
        int contentWidth = component.canScroll() ? b.width() - SCROLLBAR_WIDTH - 1 : b.width();

        var window = Minecraft.getInstance().getWindow();
        double scale = window.getGuiScale();
        int sx = (int) (b.x() * scale);
        int sy = (int) ((window.getGuiScaledHeight() - b.bottom()) * scale);
        int sw = (int) (contentWidth * scale);
        int sh = (int) (b.height() * scale);
        com.mojang.blaze3d.platform.GlStateManager._enableScissorTest();
        com.mojang.blaze3d.platform.GlStateManager._scissorBox(sx, sy, sw, sh);

        int offset = component.getScrollOffset();
        WidgetRendererRegistry reg = WidgetRendererRegistry.instance();

        for (UIComponent child : component.getChildren()) {
            if (!child.isVisible()) continue;
            Bounds cb = child.getBounds();
            if (cb == null) continue;

            Bounds shifted = Bounds.of(cb.x(), cb.y() - offset, cb.width(), cb.height());
            child.setBounds(shifted);

            if (shifted.bottom() > b.y() && shifted.y() < b.bottom()) {
                @SuppressWarnings("unchecked")
                WidgetRenderer<UIComponent> renderer = reg.get(child.getType());
                if (renderer != null) {
                    renderer.render(graphics, child, viewModel, theme, mouseX, mouseY + offset, RenderLayer.FOREGROUND);
                }
            }

            child.setBounds(cb);
        }

        com.mojang.blaze3d.platform.GlStateManager._disableScissorTest();
    }

    private void renderScrollbar(GuiGraphics graphics, ScrollPanelComponent component, Bounds b) {
        int trackX = b.right() - SCROLLBAR_WIDTH;
        graphics.fill(trackX, b.y(), b.right(), b.bottom(), SCROLLBAR_COLOR);

        float viewH = b.height();
        float contentH = component.getContentHeight();
        float thumbH = Math.max(10, viewH * viewH / contentH);
        float thumbY = b.y() + (component.getScrollOffset() / (contentH - viewH)) * (viewH - thumbH);

        graphics.fill(trackX, (int) thumbY, b.right(), (int) (thumbY + thumbH), SCROLLBAR_THUMB);
    }
}