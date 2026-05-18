package dev.uiweaver.client.render;

import dev.uiweaver.api.component.ComponentType;
import dev.uiweaver.api.component.UIComponent;

import java.util.EnumMap;
import java.util.Map;

public class WidgetRendererRegistry<G> {

    private static WidgetRendererRegistry<?> INSTANCE;

    private final Map<ComponentType, WidgetRenderer<? extends UIComponent, G>> renderers = new EnumMap<>(ComponentType.class);

    protected WidgetRendererRegistry() {}

    @SuppressWarnings("unchecked")
    public static <G> WidgetRendererRegistry<G> instance() {
        if (INSTANCE == null) INSTANCE = new WidgetRendererRegistry<>();
        return (WidgetRendererRegistry<G>) INSTANCE;
    }

    public <T extends UIComponent> void register(ComponentType type, WidgetRenderer<T, G> renderer) {
        renderers.put(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public <T extends UIComponent> WidgetRenderer<T, G> get(ComponentType type) {
        return (WidgetRenderer<T, G>) renderers.get(type);
    }
}