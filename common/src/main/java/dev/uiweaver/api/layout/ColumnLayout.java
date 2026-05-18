package dev.uiweaver.api.layout;

import dev.uiweaver.api.component.AbstractComponent;
import dev.uiweaver.api.component.UIComponent;

import java.util.List;

public class ColumnLayout implements LayoutEngine {

    private final Insets padding;
    private final int gap;
    private final Alignment crossAxis;

    public ColumnLayout(Insets padding, int gap, Alignment crossAxis) {
        this.padding = padding;
        this.gap = gap;
        this.crossAxis = crossAxis;
    }

    @Override
    public void layout(List<UIComponent> children, Bounds container) {
        int x = container.x() + padding.left();
        int y = container.y() + padding.top();
        int innerWidth = container.width() - padding.horizontal();

        for (UIComponent child : children) {
            if (!child.isVisible()) continue;

            Size preferred = child instanceof AbstractComponent ac ? ac.getPreferredSize() : Size.WRAP;

            int childWidth = preferred.fillsWidth() ? innerWidth
                    : preferred.isFixedWidth() ? preferred.width()
                      : innerWidth;

            int childHeight = preferred.isFixedHeight() ? preferred.height() : 10;

            int childX = switch (crossAxis) {
                case CENTER -> x + (innerWidth - childWidth) / 2;
                case END -> x + innerWidth - childWidth;
                default -> x;
            };

            child.setBounds(Bounds.of(childX, y, childWidth, childHeight));
            y += childHeight + gap;
        }
    }

    @Override
    public Size measure(List<UIComponent> children, Bounds container) {
        int totalHeight = padding.top() + padding.bottom();
        boolean first = true;
        for (UIComponent child : children) {
            if (!child.isVisible()) continue;
            if (!first) totalHeight += gap;
            Size preferred = child instanceof AbstractComponent ac ? ac.getPreferredSize() : Size.WRAP;
            totalHeight += preferred.isFixedHeight() ? preferred.height() : 10;
            first = false;
        }
        return Size.fixed(container.width(), totalHeight);
    }
}