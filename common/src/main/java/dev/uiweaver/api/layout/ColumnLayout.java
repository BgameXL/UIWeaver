package dev.uiweaver.api.layout;

import dev.uiweaver.api.component.AbstractComponent;
import dev.uiweaver.api.component.Measurable;
import dev.uiweaver.api.component.UIComponent;

import java.util.List;

public class ColumnLayout implements LayoutEngine {

    private final Insets padding;
    private final int gap;
    private final Alignment crossAxis;

    public ColumnLayout(Insets padding, int gap, Alignment crossAxis) {
        this.padding   = padding;
        this.gap       = gap;
        this.crossAxis = crossAxis;
    }

    @Override
    public void layout(List<UIComponent> children, Bounds container) {
        int innerW = container.width()  - padding.horizontal();
        int innerH = container.height() - padding.vertical();
        List<UIComponent> visible = children.stream().filter(UIComponent::isVisible).toList();

        int[] heights  = new int[visible.size()];
        int totalFixed = 0;
        int gapTotal   = gap * Math.max(0, visible.size() - 1);
        int totalGrow  = 0;

        for (int i = 0; i < visible.size(); i++) {
            Size pref = pref(visible.get(i));
            if (pref.fillsHeight() || pref.grows()) {
                totalGrow += Math.max(1, pref.getGrow());
            } else {
                int h = resolveHeight(visible.get(i), pref, innerW, innerH);
                heights[i]  = h;
                totalFixed += h;
            }
        }

        int remaining = Math.max(0, innerH - totalFixed - gapTotal);
        for (int i = 0; i < visible.size(); i++) {
            Size pref = pref(visible.get(i));
            if (pref.fillsHeight() || pref.grows()) {
                int weight = Math.max(1, pref.getGrow());
                heights[i] = pref.clampHeight(totalGrow > 0 ? (remaining * weight / totalGrow) : 0);
            }
        }

        int y = container.y() + padding.top();
        for (int i = 0; i < visible.size(); i++) {
            UIComponent child = visible.get(i);
            Size pref = pref(child);

            int childW = pref.fillsWidth()  ? innerW
                    : pref.isFixedWidth() ? pref.clampWidth(pref.width())
                      : resolveWidth(child, pref, innerW, heights[i]);

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
        int innerW  = container.width() - padding.horizontal();
        int totalH  = padding.vertical();
        boolean first = true;

        for (UIComponent child : children) {
            if (!child.isVisible()) continue;
            if (!first) totalH += gap;
            Size pref = pref(child);
            totalH += resolveHeight(child, pref, innerW, 0);
            first = false;
        }
        return Size.fixed(innerW + padding.horizontal(), totalH);
    }

    private static int resolveHeight(UIComponent c, Size pref, int availW, int availH) {
        if (pref.isFixedHeight()) return pref.clampHeight(pref.height());
        if (pref.wrapsHeight() && c instanceof Measurable m) return pref.clampHeight(m.measure(availW, availH).height());
        return pref.clampHeight(10);
    }

    private static int resolveWidth(UIComponent c, Size pref, int availW, int availH) {
        if (pref.isFixedWidth()) return pref.clampWidth(pref.width());
        if (pref.wrapsWidth() && c instanceof Measurable m) return pref.clampWidth(m.measure(availW, availH).width());
        return availW;
    }

    private static Size pref(UIComponent c) {
        return c instanceof AbstractComponent ac ? ac.getPreferredSize() : Size.WRAP;
    }
}