package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Insets;
import dev.uiweaver.api.layout.StackLayout;

import java.util.ArrayList;
import java.util.List;

public class StackBuilder extends ComponentBuilder<StackBuilder> {

    private Insets padding = Insets.NONE;
    private final List<UIComponent> children = new ArrayList<>();

    public StackBuilder padding(int all)      { this.padding = Insets.all(all);   return this; }
    public StackBuilder padding(int v, int h) { this.padding = Insets.of(v, h);   return this; }

    public StackBuilder add(ComponentBuilder<?> child) { children.add(child.build()); return this; }
    public StackBuilder add(UIComponent c)             { children.add(c);             return this; }

    @Override
    public PanelComponent build() {
        return new PanelComponent(id, visible, enabled, preferredSize, new StackLayout(padding), children);
    }
}