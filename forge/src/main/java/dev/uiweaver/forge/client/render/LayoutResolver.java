package dev.uiweaver.forge.client.render;

import dev.uiweaver.api.component.AbstractComponent;
import dev.uiweaver.api.component.LayoutContainer;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.layout.Size;

public class LayoutResolver {

    public static void resolve(UIComponent root, int x, int y, int width, int height) {
        root.setBounds(Bounds.of(x, y, width, height));
        resolveChildren(root);
    }

    private static void resolveChildren(UIComponent component) {
        if (component.getChildren().isEmpty()) return;

        if (component instanceof LayoutContainer container) {
            container.getLayoutEngine().layout(component.getChildren(), component.getBounds());
        }

        for (UIComponent child : component.getChildren()) {
            // Only enforce fixed preferred size — fill/grow/wrap are resolved by the layout engine
            if (child instanceof AbstractComponent ac) {
                Size pref = ac.getPreferredSize();
                Bounds current = child.getBounds();
                if (current != null && pref.isFixedWidth() && pref.isFixedHeight()) {
                    int w = pref.clampWidth(pref.width());
                    int h = pref.clampHeight(pref.height());
                    if (w != current.width() || h != current.height()) {
                        child.setBounds(Bounds.of(current.x(), current.y(), w, h));
                    }
                }
            }
            resolveChildren(child);
        }
    }
}