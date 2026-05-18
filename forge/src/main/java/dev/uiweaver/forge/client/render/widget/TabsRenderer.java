package dev.uiweaver.forge.client.render.widget;

import dev.uiweaver.api.component.TabsComponent;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.theme.UITheme;
import dev.uiweaver.forge.client.render.ForgeWidgetRenderer;
import dev.uiweaver.forge.client.render.ForgeWidgetRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class TabsRenderer implements ForgeWidgetRenderer<TabsComponent> {

    private static final int TAB_ACTIVE_BG   = 0xFF3A3A3A;
    private static final int TAB_INACTIVE_BG = 0xFF252525;
    private static final int TAB_BORDER      = 0xFF555555;
    private static final int CONTENT_BG      = 0xFF2B2B2B;

    @Override
    public void render(GuiGraphics graphics, TabsComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        Bounds b = component.getBounds();
        if (b == null) return;

        if (layer == RenderLayer.BACKGROUND) {
            renderTabBar(graphics, component, theme, b, mouseX, mouseY);
            Bounds cb = component.getContentBounds();
            if (cb != null) {
                graphics.fill(cb.x(), cb.y(), cb.right(), cb.bottom(), CONTENT_BG);
            }
        }

        if (layer == RenderLayer.FOREGROUND || layer == RenderLayer.BACKGROUND) {
            renderActiveContent(graphics, component, viewModel, theme, mouseX, mouseY, layer);
        }
    }

    private void renderTabBar(GuiGraphics graphics, TabsComponent component,
                              UITheme theme, Bounds b, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        int tabW  = component.getTabWidth();
        int tabH  = TabsComponent.TAB_HEIGHT;
        int hovered = component.getTabAt(mouseX, mouseY);

        for (int i = 0; i < component.getTabCount(); i++) {
            int tx  = b.x() + i * tabW;
            int ty  = b.y();
            boolean active = i == component.getActiveTab();

            int bg = active ? TAB_ACTIVE_BG : (i == hovered ? theme.getButtonHoverColor() : TAB_INACTIVE_BG);
            graphics.fill(tx, ty, tx + tabW, ty + tabH, bg);
            graphics.renderOutline(tx, ty, tabW, tabH, TAB_BORDER);

            Component label = component.getTabLabels().get(i);
            int textW = font.width(label);
            int textX = tx + (tabW - textW) / 2;
            int textY = ty + (tabH - font.lineHeight) / 2;
            int color = active ? theme.getTextColor() : theme.getTextColorDisabled();
            graphics.drawString(font, label, textX, textY, color, false);
        }
    }

    private void renderActiveContent(GuiGraphics graphics, TabsComponent component,
                                     UIViewModel viewModel, UITheme theme,
                                     int mouseX, int mouseY, RenderLayer layer) {
        UIComponent content = component.getActiveContent();
        if (content == null || !content.isVisible()) return;

        Bounds cb = component.getContentBounds();
        if (cb == null) return;

        if (content.getBounds() == null) {
            content.setBounds(cb);
        }

        ForgeWidgetRendererRegistry reg = ForgeWidgetRendererRegistry.instance();
        renderTree(graphics, content, viewModel, theme, mouseX, mouseY, layer, reg);
    }

    @SuppressWarnings("unchecked")
    private void renderTree(GuiGraphics graphics, UIComponent component, UIViewModel viewModel,
                            UITheme theme, int mouseX, int mouseY, RenderLayer layer,
                            ForgeWidgetRendererRegistry reg) {
        if (!component.isVisible()) return;
        var renderer = reg.get(component.getType());
        if (renderer != null) renderer.render(graphics, component, viewModel, theme, mouseX, mouseY, layer);
        for (UIComponent child : component.getChildren()) {
            renderTree(graphics, child, viewModel, theme, mouseX, mouseY, layer, reg);
        }
    }
}