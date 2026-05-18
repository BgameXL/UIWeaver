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
        int innerW = container.width()  - padding.horizontal();
        int innerH = container.height() - padding.vertical();
        List<UIComponent> visible = children.stream().filter(UIComponent::isVisible).toList();

        // measure pass — resolve fixed and wrap heights, count fill/grow children
        int[] heights  = new int[visible.size()];
        int totalFixed = 0;
        int gapTotal   = gap * Math.max(0, visible.size() - 1);
        int totalGrow  = 0;

        for (int i = 0; i < visible.size(); i++) {
            Size pref = pref(visible.get(i));
            if (pref.fillsHeight() || pref.grows()) {
                totalGrow += Math.max(1, pref.getGrow());
            } else {
                int h = pref.isFixedHeight() ? pref.height() : 10;
                h = pref.clampHeight(h);
                heights[i] = h;
                totalFixed += h;
            }
        }

        // distribute remaining space to fill/grow children
        int remaining = Math.max(0, innerH - totalFixed - gapTotal);
        for (int i = 0; i < visible.size(); i++) {
            Size pref = pref(visible.get(i));
            if (pref.fillsHeight() || pref.grows()) {
                int weight = Math.max(1, pref.getGrow());
                int h = totalGrow > 0 ? (remaining * weight / totalGrow) : 0;
                heights[i] = pref.clampHeight(h);
            }
        }

        // layout pass — assign bounds
        int y = container.y() + padding.top();
        for (int i = 0; i < visible.size(); i++) {
            UIComponent child = visible.get(i);
            Size pref = pref(child);

            int childW = pref.fillsWidth() ? innerW
                    : pref.isFixedWidth()  ? pref.clampWidth(pref.width())
                      : innerW;

            int childX = switch (crossAxis) {
                case CENTER -> container.x() + padding.left() + (innerW - childW) / 2;
                case END    -> container.x() + padding.left() + innerW - childW;
                default     -> container.x() + padding.left();
            };

            child.setBounds(Bounds.of(childX, y, childW, heights[i]));
            y += heights[i] + gap;
        }
    }

    @Override
    public Size measure(List<UIComponent> children, Bounds container) {
        int innerW   = container.width() - padding.horizontal();
        int totalH   = padding.vertical();
        boolean first = true;

        for (UIComponent child : children) {
            if (!child.isVisible()) continue;
            if (!first) totalH += gap;
            Size pref = pref(child);
            totalH += pref.isFixedHeight() ? pref.height() : 10;
            first = false;
        }
        return Size.fixed(innerW + padding.horizontal(), totalH);
    }

    private static Size pref(UIComponent c) {
        return c instanceof AbstractComponent ac ? ac.getPreferredSize() : Size.WRAP;
    }
}