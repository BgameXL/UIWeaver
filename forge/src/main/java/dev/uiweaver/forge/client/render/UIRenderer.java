package dev.uiweaver.forge.client.render;

import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.render.RenderLayer;
import dev.uiweaver.client.render.WidgetRenderer;
import dev.uiweaver.client.render.WidgetRendererRegistry;
import dev.uiweaver.client.theme.ThemeRegistry;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.gui.GuiGraphics;

public class UIRenderer {

    private final UIScreenSpec spec;
    private final UIViewModel viewModel;
    private final WidgetRendererRegistry renderers = WidgetRendererRegistry.instance();
    private int leftPos, topPos, width, height;

    public UIRenderer(UIScreenSpec spec, UIViewModel viewModel) {
        this.spec = spec;
        this.viewModel = viewModel;
    }

    public void init(int leftPos, int topPos, int width, int height) {
        this.leftPos = leftPos;
        this.topPos = topPos;
        this.width = width;
        this.height = height;
        LayoutResolver.resolve(spec.getRoot(), leftPos, topPos, width, height);
    }

    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY) {
        UITheme theme = ThemeRegistry.getActive();
        UIBackground.render(graphics, theme, leftPos, topPos, width, height);
        renderComponent(graphics, spec.getRoot(), theme, mouseX, mouseY, RenderLayer.BACKGROUND);
    }

    public void renderForeground(GuiGraphics graphics, int mouseX, int mouseY) {
        UITheme theme = ThemeRegistry.getActive();
        renderComponent(graphics, spec.getRoot(), theme, mouseX, mouseY, RenderLayer.FOREGROUND);
        renderComponent(graphics, spec.getRoot(), theme, mouseX, mouseY, RenderLayer.TOOLTIP);
    }

    @SuppressWarnings("unchecked")
    private void renderComponent(GuiGraphics graphics, UIComponent component,
                                 UITheme theme, int mouseX, int mouseY, RenderLayer layer) {
        if (!component.isVisible()) return;

        WidgetRenderer<UIComponent> renderer = renderers.get(component.getType());
        if (renderer != null) {
            renderer.render(graphics, component, viewModel, theme, mouseX, mouseY, layer);
        }

        for (UIComponent child : component.getChildren()) {
            renderComponent(graphics, child, theme, mouseX, mouseY, layer);
        }
    }

    public void onViewModelUpdated(String key) {
        applyBindings(spec.getRoot(), key);
    }

    private void applyBindings(UIComponent component, String changedKey) {
        spec.getBindings().stream()
                .filter(b -> b.syncKey().equals(changedKey) && b.componentId().equals(component.getId()))
                .forEach(b -> {
                    if (component instanceof dev.uiweaver.api.binding.Bindable bindable) {
                        viewModel.get(changedKey).ifPresent(v -> bindable.applyBinding(b.property(), v));
                    }
                });
        for (UIComponent child : component.getChildren()) {
            applyBindings(child, changedKey);
        }
    }
}