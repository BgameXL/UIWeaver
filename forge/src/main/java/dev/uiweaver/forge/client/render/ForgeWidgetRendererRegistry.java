package dev.uiweaver.forge.client.render;

import dev.uiweaver.api.component.ComponentType;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.client.render.WidgetRendererRegistry;
import net.minecraft.client.gui.GuiGraphics;

public class ForgeWidgetRendererRegistry extends WidgetRendererRegistry<GuiGraphics> {

    private static final ForgeWidgetRendererRegistry INSTANCE = new ForgeWidgetRendererRegistry();

    private ForgeWidgetRendererRegistry() {}

    public static ForgeWidgetRendererRegistry instance() {
        return INSTANCE;
    }

    public <T extends UIComponent> void register(ComponentType type, ForgeWidgetRenderer<T> renderer) {
        super.register(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public <T extends UIComponent> ForgeWidgetRenderer<T> get(ComponentType type) {
        return (ForgeWidgetRenderer<T>) super.get(type);
    }
}