package dev.uiweaver.api.layout;

import dev.uiweaver.api.component.UIComponent;

import java.util.List;

public class StackLayout implements LayoutEngine {

    private final Insets padding;

    public StackLayout(Insets padding) {
        this.padding = padding;
    }

    public StackLayout() {
        this(Insets.NONE);
    }

    @Override
    public void layout(List<UIComponent> children, Bounds container) {
        int x = container.x() + padding.left();
        int y = container.y() + padding.top();
        int w = container.width()  - padding.horizontal();
        int h = container.height() - padding.vertical();

        for (UIComponent child : children) {
            if (!child.isVisible()) continue;
            child.setBounds(Bounds.of(x, y, w, h));
        }
    }

    @Override
    public Size measure(List<UIComponent> children, Bounds container) {
        return Size.fixed(container.width(), container.height());
    }
}