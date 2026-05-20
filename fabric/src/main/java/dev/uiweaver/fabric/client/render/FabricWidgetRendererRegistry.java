package dev.uiweaver.fabric.client.render;

import dev.uiweaver.api.component.ComponentType;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.client.render.WidgetRendererRegistry;
import net.minecraft.client.gui.GuiGraphics;

public class FabricWidgetRendererRegistry extends WidgetRendererRegistry<GuiGraphics> {

    private static final FabricWidgetRendererRegistry INSTANCE = new FabricWidgetRendererRegistry();

    private FabricWidgetRendererRegistry() {}

    public static FabricWidgetRendererRegistry instance() {
        return INSTANCE;
    }

    public <T extends UIComponent> void register(ComponentType type, FabricWidgetRenderer<T> renderer) {
        super.register(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public <T extends UIComponent> FabricWidgetRenderer<T> get(ComponentType type) {
        return (FabricWidgetRenderer<T>) super.get(type);
    }
}