package dev.uiweaver.client.render;

import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.theme.UITheme;

public interface WidgetRenderer<T extends UIComponent, G> {

    void render(G graphics, T component, UIViewModel viewModel,
                UITheme theme, int mouseX, int mouseY, RenderLayer layer);
}