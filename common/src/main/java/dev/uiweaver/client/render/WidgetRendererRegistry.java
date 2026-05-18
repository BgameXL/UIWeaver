package dev.uiweaver.client.render;

import dev.uiweaver.api.component.ComponentType;
import dev.uiweaver.api.component.UIComponent;

import java.util.EnumMap;
import java.util.Map;

public class WidgetRendererRegistry {

    private static final WidgetRendererRegistry INSTANCE = new WidgetRendererRegistry();

    private final Map<ComponentType, WidgetRenderer<? extends UIComponent>> renderers = new EnumMap<>(ComponentType.class);

    private WidgetRendererRegistry() {}

    public static WidgetRendererRegistry instance() {
        return INSTANCE;
    }

    public <T extends UIComponent> void register(ComponentType type, WidgetRenderer<T> renderer) {
        renderers.put(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public <T extends UIComponent> WidgetRenderer<T> get(ComponentType type) {
        return (WidgetRenderer<T>) renderers.get(type);
    }
}