package dev.uiweaver.api.component;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class TabsBuilder extends ComponentBuilder<TabsBuilder> {

    private final List<Component> labels   = new ArrayList<>();
    private final List<UIComponent> contents = new ArrayList<>();

    public TabsBuilder tab(String label, ComponentBuilder<?> content) {
        return tab(Component.literal(label), content.build());
    }

    public TabsBuilder tab(String label, UIComponent content) {
        return tab(Component.literal(label), content);
    }

    public TabsBuilder tab(Component label, UIComponent content) {
        labels.add(label);
        contents.add(content);
        return this;
    }

    @Override
    public TabsComponent build() {
        if (labels.isEmpty()) throw new IllegalStateException("TabsBuilder requires at least one tab");
        return new TabsComponent(id, visible, enabled, preferredSize, labels, contents);
    }
}