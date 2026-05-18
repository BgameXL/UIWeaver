package dev.uiweaver.api.layout;

import dev.uiweaver.api.component.AbstractComponent;
import dev.uiweaver.api.component.UIComponent;

import java.util.List;

public class RowLayout implements LayoutEngine {

    private final Insets padding;
    private final int gap;
    private final Alignment crossAxis;

    public RowLayout(Insets padding, int gap, Alignment crossAxis) {
        this.padding = padding;
        this.gap = gap;
        this.crossAxis = crossAxis;
    }

    @Override
    public void layout(List<UIComponent> children, Bounds container) {
        int x = container.x() + padding.left();
        int innerHeight = container.height() - padding.vertical();

        for (UIComponent child : children) {
            if (!child.isVisible()) continue;

            Size preferred = child instanceof AbstractComponent ac ? ac.getPreferredSize() : Size.WRAP;

            int childWidth  = preferred.isFixedWidth()  ? preferred.width()  : 16;
            int childHeight = preferred.fillsHeight()   ? innerHeight
                    : preferred.isFixedHeight() ? preferred.height()
                      : innerHeight;

            int childY = switch (crossAxis) {
                case CENTER -> container.y() + padding.top() + (innerHeight - childHeight) / 2;
                case END    -> container.y() + padding.top() + innerHeight - childHeight;
                default     -> container.y() + padding.top();
            };

            child.setBounds(Bounds.of(x, childY, childWidth, childHeight));
            x += childWidth + gap;
        }
    }

    @Override
    public Size measure(List<UIComponent> children, Bounds container) {
        int totalWidth = padding.left() + padding.right();
        boolean first = true;
        for (UIComponent child : children) {
            if (!child.isVisible()) continue;
            if (!first) totalWidth += gap;
            Size preferred = child instanceof AbstractComponent ac ? ac.getPreferredSize() : Size.WRAP;
            totalWidth += preferred.isFixedWidth() ? preferred.width() : 16;
            first = false;
        }
        return Size.fixed(totalWidth, container.height());
    }
}