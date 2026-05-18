package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.GridLayout;
import dev.uiweaver.api.layout.Insets;
import dev.uiweaver.api.layout.LayoutEngine;

import java.util.ArrayList;
import java.util.List;

public class GridBuilder extends ComponentBuilder<GridBuilder> {

    private final int columns;
    private int gapH    = 0;
    private int gapV    = 0;
    private Insets padding = Insets.NONE;
    private final List<UIComponent> children = new ArrayList<>();

    public GridBuilder(int columns) {
        this.columns = columns;
    }

    public GridBuilder gapH(int g)        { this.gapH    = g;         return this; }
    public GridBuilder gapV(int g)        { this.gapV    = g;         return this; }
    public GridBuilder gap(int g)         { this.gapH = this.gapV = g; return this; }
    public GridBuilder padding(int all)   { this.padding = Insets.all(all); return this; }
    public GridBuilder padding(int v, int h) { this.padding = Insets.of(v, h); return this; }

    public GridBuilder add(ComponentBuilder<?> child) { children.add(child.build()); return this; }
    public GridBuilder add(UIComponent c)             { children.add(c);             return this; }

    @Override
    public PanelComponent build() {
        LayoutEngine layout = new GridLayout(columns, gapH, gapV, padding);
        return new PanelComponent(id, visible, enabled, preferredSize, layout, children);
    }
}