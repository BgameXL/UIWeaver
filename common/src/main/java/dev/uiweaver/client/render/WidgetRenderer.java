package dev.uiweaver.client.render;

import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.gui.GuiGraphics;

public interface WidgetRenderer<T extends UIComponent> {

    void render(GuiGraphics graphics, T component, UIViewModel viewModel,
                UITheme theme, int mouseX, int mouseY, RenderLayer layer);
}