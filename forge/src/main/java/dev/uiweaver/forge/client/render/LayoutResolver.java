package dev.uiweaver.forge.client.render;

import dev.uiweaver.api.component.AbstractComponent;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.layout.Size;
import dev.uiweaver.client.render.LayoutAware;

public class LayoutResolver {

    public static void resolve(UIComponent root, int x, int y, int width, int height) {
        root.setBounds(Bounds.of(x, y, width, height));
        resolveChildren(root);
    }

    private static void resolveChildren(UIComponent component) {
        if (component.getChildren().isEmpty()) return;

        if (component instanceof LayoutAware layoutAware) {
            layoutAware.getLayoutEngine().layout(component.getChildren(), component.getBounds());
        }

        for (UIComponent child : component.getChildren()) {
            // After parent assigned bounds, enforce fixed preferredSize
            if (child instanceof AbstractComponent ac) {
                Size pref = ac.getPreferredSize();
                Bounds current = child.getBounds();
                if (current != null) {
                    int w = pref.isFixedWidth()  ? pref.width()  : current.width();
                    int h = pref.isFixedHeight() ? pref.height() : current.height();
                    if (w != current.width() || h != current.height()) {
                        child.setBounds(Bounds.of(current.x(), current.y(), w, h));
                    }
                }
            }
            resolveChildren(child);
        }
    }
}