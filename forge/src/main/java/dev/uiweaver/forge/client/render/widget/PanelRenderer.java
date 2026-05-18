package dev.uiweaver.forge.client.render.widget;

import dev.uiweaver.api.component.PanelComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.render.WidgetRenderer;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.gui.GuiGraphics;

public class PanelRenderer implements WidgetRenderer<PanelComponent> {

    @Override
    public void render(GuiGraphics graphics, PanelComponent component, UIViewModel viewModel,
                       UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        // panels are transparent by default — only draw if they have explicit style
        // the root panel background is drawn by UIBackground separately
    }
}