package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Alignment;
import dev.uiweaver.api.layout.Insets;
import dev.uiweaver.api.layout.LayoutEngine;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class PanelBuilder<T extends PanelBuilder<T>> extends ComponentBuilder<T> {

    protected Insets padding = Insets.NONE;
    protected int gap = 0;
    protected Alignment crossAxis = Alignment.START;
    protected final List<UIComponent> children = new ArrayList<>();

    public T padding(int all) { this.padding = Insets.all(all); return (T) this; }
    public T padding(int v, int h) { this.padding = Insets.of(v, h); return (T) this; }
    public T gap(int gap) { this.gap = gap; return (T) this; }
    public T align(Alignment a) { this.crossAxis = a; return (T) this; }

    public T add(ComponentBuilder<?> child) { this.children.add(child.build()); return (T) this; }
    public T add(UIComponent component) { this.children.add(component); return (T) this; }

    protected abstract LayoutEngine createLayout();

    @Override
    public PanelComponent build() {
        return new PanelComponent(id, visible, enabled, preferredSize, createLayout(), children);
    }
}