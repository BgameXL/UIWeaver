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
        int innerW = container.width()  - padding.horizontal();
        int innerH = container.height() - padding.vertical();
        List<UIComponent> visible = children.stream().filter(UIComponent::isVisible).toList();

        int[] widths   = new int[visible.size()];
        int totalFixed = 0;
        int gapTotal   = gap * Math.max(0, visible.size() - 1);
        int totalGrow  = 0;

        for (int i = 0; i < visible.size(); i++) {
            Size pref = pref(visible.get(i));
            if (pref.fillsWidth() || pref.grows()) {
                totalGrow += Math.max(1, pref.getGrow());
            } else {
                int w = pref.isFixedWidth() ? pref.width() : 16;
                w = pref.clampWidth(w);
                widths[i] = w;
                totalFixed += w;
            }
        }

        int remaining = Math.max(0, innerW - totalFixed - gapTotal);
        for (int i = 0; i < visible.size(); i++) {
            Size pref = pref(visible.get(i));
            if (pref.fillsWidth() || pref.grows()) {
                int weight = Math.max(1, pref.getGrow());
                int w = totalGrow > 0 ? (remaining * weight / totalGrow) : 0;
                widths[i] = pref.clampWidth(w);
            }
        }

        int x = container.x() + padding.left();
        for (int i = 0; i < visible.size(); i++) {
            UIComponent child = visible.get(i);
            Size pref = pref(child);

            int childH = pref.fillsHeight() ? innerH
                    : pref.isFixedHeight()  ? pref.clampHeight(pref.height())
                      : innerH;

            int childY = switch (crossAxis) {
                case CENTER -> container.y() + padding.top() + (innerH - childH) / 2;
                case END    -> container.y() + padding.top() + innerH - childH;
                default     -> container.y() + padding.top();
            };

            child.setBounds(Bounds.of(x, childY, widths[i], childH));
            x += widths[i] + gap;
        }
    }

    @Override
    public Size measure(List<UIComponent> children, Bounds container) {
        int innerH   = container.height() - padding.vertical();
        int totalW   = padding.horizontal();
        boolean first = true;

        for (UIComponent child : children) {
            if (!child.isVisible()) continue;
            if (!first) totalW += gap;
            Size pref = pref(child);
            totalW += pref.isFixedWidth() ? pref.width() : 16;
            first = false;
        }
        return Size.fixed(totalW, innerH + padding.vertical());
    }

    private static Size pref(UIComponent c) {
        return c instanceof AbstractComponent ac ? ac.getPreferredSize() : Size.WRAP;
    }
}