package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Alignment;
import dev.uiweaver.api.layout.ColumnLayout;
import dev.uiweaver.api.layout.Insets;

import java.util.ArrayList;
import java.util.List;

public class ScrollPanelBuilder extends ComponentBuilder<ScrollPanelBuilder> {

    private final List<UIComponent> children = new ArrayList<>();
    private Insets padding = Insets.NONE;
    private int gap = 0;

    public ScrollPanelBuilder padding(int all) { this.padding = Insets.all(all); return this; }
    public ScrollPanelBuilder gap(int gap) { this.gap = gap; return this; }
    public ScrollPanelBuilder add(ComponentBuilder<?> child) { children.add(child.build()); return this; }
    public ScrollPanelBuilder add(UIComponent c) { children.add(c); return this; }

    @Override
    public ScrollPanelComponent build() {
        return new ScrollPanelComponent(id, visible, enabled, preferredSize,
                new ColumnLayout(padding, gap, Alignment.START), children);
    }
}