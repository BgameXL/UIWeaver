package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.LayoutEngine;
import dev.uiweaver.api.layout.Size;

import java.util.List;

public class PanelComponent extends AbstractComponent implements LayoutContainer {

    private final LayoutEngine layoutEngine;

    public PanelComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                          LayoutEngine layoutEngine, List<UIComponent> children) {
        super(id, visible, enabled, preferredSize);
        this.layoutEngine = layoutEngine;
        this.children.addAll(children);
    }

    @Override public ComponentType getType() { return ComponentType.PANEL; }
    @Override public LayoutEngine getLayoutEngine() { return layoutEngine; }
}