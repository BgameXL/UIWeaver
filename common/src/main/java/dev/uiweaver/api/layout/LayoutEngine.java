package dev.uiweaver.api.layout;

import dev.uiweaver.api.component.UIComponent;

import java.util.List;

public interface LayoutEngine {

    void layout(List<UIComponent> children, Bounds container);

    Size measure(List<UIComponent> children, Bounds container);
}
