package dev.uiweaver.forge.client.render.widget;

import dev.uiweaver.api.component.ScrollPanelComponent;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.theme.UITheme;
import dev.uiweaver.forge.client.render.ForgeWidgetRenderer;
import dev.uiweaver.forge.client.render.ForgeWidgetRendererRegistry;
import net.minecraft.client.gui.GuiGraphics;

public class ScrollPanelRenderer implements ForgeWidgetRenderer<ScrollPanelComponent> {

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
            if (component.canScroll()) renderScrollbar(graphics, component, b);
        }

        if (layer == RenderLayer.FOREGROUND) {
            int contentWidth = component.canScroll() ? b.width() - SCROLLBAR_WIDTH - 1 : b.width();
            renderChildren(graphics, component, viewModel, theme, mouseX, mouseY, b, contentWidth);
        }
    }

    private void renderChildren(GuiGraphics graphics, ScrollPanelComponent component,
                                UIViewModel viewModel, UITheme theme,
                                int mouseX, int mouseY, Bounds b, int contentWidth) {
        int offset = component.getScrollOffset();

        graphics.enableScissor(b.x(), b.y(), b.x() + contentWidth, b.bottom());

        ForgeWidgetRendererRegistry reg = ForgeWidgetRendererRegistry.instance();

        for (UIComponent child : component.getChildren()) {
            if (!child.isVisible()) continue;
            Bounds cb = child.getBounds();
            if (cb == null) continue;

            Bounds shifted = Bounds.of(cb.x(), cb.y() - offset, cb.width(), cb.height());

            if (shifted.bottom() <= b.y() || shifted.y() >= b.bottom()) continue;

            child.setBounds(shifted);

            @SuppressWarnings("unchecked")
            var renderer = reg.get(child.getType());
            if (renderer != null) {
                renderer.render(graphics, child, viewModel, theme,
                        mouseX, mouseY + offset, RenderLayer.BACKGROUND);
                renderer.render(graphics, child, viewModel, theme,
                        mouseX, mouseY + offset, RenderLayer.FOREGROUND);
            }

            child.setBounds(cb);
        }

        graphics.disableScissor();
    }

    private void renderScrollbar(GuiGraphics graphics, ScrollPanelComponent component, Bounds b) {
        int trackX = b.right() - SCROLLBAR_WIDTH;
        graphics.fill(trackX, b.y(), b.right(), b.bottom(), SCROLLBAR_COLOR);

        float viewH    = b.height();
        float contentH = component.getContentHeight();
        float thumbH   = Math.max(10, viewH * viewH / contentH);
        float maxScroll = contentH - viewH;
        float thumbY   = maxScroll > 0
                ? b.y() + (component.getScrollOffset() / maxScroll) * (viewH - thumbH)
                : b.y();

        graphics.fill(trackX, (int) thumbY, b.right(), (int) (thumbY + thumbH), SCROLLBAR_THUMB);
    }
}