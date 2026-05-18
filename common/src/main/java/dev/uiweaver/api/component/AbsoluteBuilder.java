package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.AbsoluteLayout;
import dev.uiweaver.api.layout.Bounds;

import java.util.ArrayList;
import java.util.List;

public class AbsoluteBuilder extends ComponentBuilder<AbsoluteBuilder> {

    private final List<UIComponent> children = new ArrayList<>();

    public AbsoluteBuilder add(ComponentBuilder<?> child, int x, int y) {
        UIComponent c = child.build();
        c.setBounds(Bounds.of(x, y, 0, 0)); // x/y used as offsets by AbsoluteLayout
        children.add(c);
        return this;
    }

    public AbsoluteBuilder add(UIComponent c, int x, int y) {
        c.setBounds(Bounds.of(x, y, 0, 0));
        children.add(c);
        return this;
    }

    @Override
    public PanelComponent build() {
        return new PanelComponent(id, visible, enabled, preferredSize, new AbsoluteLayout(), children);
    }
}