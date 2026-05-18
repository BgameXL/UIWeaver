package dev.uiweaver.api.layout;

import dev.uiweaver.api.component.AbstractComponent;
import dev.uiweaver.api.component.Measurable;
import dev.uiweaver.api.component.UIComponent;

import java.util.List;

public class AbsoluteLayout implements LayoutEngine {

    @Override
    public void layout(List<UIComponent> children, Bounds container) {
        for (UIComponent child : children) {
            if (!child.isVisible()) continue;
            if (!(child instanceof AbstractComponent ac)) continue;

            Size pref = ac.getPreferredSize();
            Bounds current = child.getBounds();

            int w, h;
            if (pref.isFixedWidth()) {
                w = pref.clampWidth(pref.width());
            } else if (pref.fillsWidth()) {
                w = container.width();
            } else if (child instanceof Measurable m) {
                w = m.measure(container.width(), container.height()).width();
            } else {
                w = current != null ? current.width() : 0;
            }

            if (pref.isFixedHeight()) {
                h = pref.clampHeight(pref.height());
            } else if (pref.fillsHeight()) {
                h = container.height();
            } else if (child instanceof Measurable m) {
                h = m.measure(w, container.height()).height();
            } else {
                h = current != null ? current.height() : 0;
            }

            int ox = current != null ? current.x() : 0;
            int oy = current != null ? current.y() : 0;
            child.setBounds(Bounds.of(container.x() + ox, container.y() + oy, w, h));
        }
    }

    @Override
    public Size measure(List<UIComponent> children, Bounds container) {
        return Size.fixed(container.width(), container.height());
    }
}