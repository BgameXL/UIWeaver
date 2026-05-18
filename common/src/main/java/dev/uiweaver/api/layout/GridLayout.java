package dev.uiweaver.api.layout;

import dev.uiweaver.api.component.AbstractComponent;
import dev.uiweaver.api.component.Measurable;
import dev.uiweaver.api.component.UIComponent;

import java.util.List;

public class GridLayout implements LayoutEngine {

    private final int columns;
    private final int gapH;
    private final int gapV;
    private final Insets padding;

    public GridLayout(int columns, int gapH, int gapV, Insets padding) {
        this.columns = Math.max(1, columns);
        this.gapH    = gapH;
        this.gapV    = gapV;
        this.padding = padding;
    }

    @Override
    public void layout(List<UIComponent> children, Bounds container) {
        List<UIComponent> visible = children.stream().filter(UIComponent::isVisible).toList();
        if (visible.isEmpty()) return;

        int innerW   = container.width() - padding.horizontal();
        int colW     = (innerW - gapH * (columns - 1)) / columns;
        int startX   = container.x() + padding.left();
        int y        = container.y() + padding.top();
        int col      = 0;
        int rowH     = 0;

        for (UIComponent child : visible) {
            Size pref = pref(child);
            int h = resolveHeight(child, pref, colW);
            int w = pref.isFixedWidth() ? Math.min(pref.width(), colW) : colW;

            int x = startX + col * (colW + gapH);
            child.setBounds(Bounds.of(x, y, w, h));

            rowH = Math.max(rowH, h);
            col++;
            if (col >= columns) {
                col  = 0;
                y   += rowH + gapV;
                rowH = 0;
            }
        }
    }

    @Override
    public Size measure(List<UIComponent> children, Bounds container) {
        List<UIComponent> visible = children.stream().filter(UIComponent::isVisible).toList();
        if (visible.isEmpty()) return Size.fixed(padding.horizontal(), padding.vertical());

        int innerW = container.width() - padding.horizontal();
        int colW   = (innerW - gapH * (columns - 1)) / columns;
        int rows   = (int) Math.ceil((double) visible.size() / columns);

        int maxRowH = 0;
        for (UIComponent child : visible) {
            Size pref = pref(child);
            maxRowH = Math.max(maxRowH, resolveHeight(child, pref, colW));
        }

        int totalH = padding.vertical() + rows * maxRowH + Math.max(0, rows - 1) * gapV;
        return Size.fixed(container.width(), totalH);
    }

    private static int resolveHeight(UIComponent c, Size pref, int availW) {
        if (pref.isFixedHeight()) return pref.clampHeight(pref.height());
        if (c instanceof Measurable m) return m.measure(availW, 0).height();
        return 16;
    }

    private static Size pref(UIComponent c) {
        return c instanceof AbstractComponent ac ? ac.getPreferredSize() : Size.WRAP;
    }
}