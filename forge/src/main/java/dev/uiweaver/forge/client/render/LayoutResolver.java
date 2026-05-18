package dev.uiweaver.forge.client.render;

import dev.uiweaver.api.component.AbstractComponent;
import dev.uiweaver.api.component.LayoutContainer;
import dev.uiweaver.api.component.ScrollPanelComponent;
import dev.uiweaver.api.component.TabsComponent;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.layout.Size;

public class LayoutResolver {

    public static void resolve(UIComponent root, int x, int y, int width, int height) {
        root.setBounds(Bounds.of(x, y, width, height));
        resolveChildren(root);
    }

    public static void resolveFrom(UIComponent root) {
        resolveChildren(root);
    }

    private static void resolveChildren(UIComponent component) {
        if (component.getChildren().isEmpty()) return;

        if (component instanceof LayoutContainer container) {
            container.getLayoutEngine().layout(component.getChildren(), component.getBounds());
        }

        if (component instanceof TabsComponent tabs) {
            var content = tabs.getActiveContent();
            var cb = tabs.getContentBounds();
            if (content != null && cb != null) {
                content.setBounds(cb);
                resolveChildren(content);
            }
        }

        if (component instanceof ScrollPanelComponent scroll) {
            int maxBottom = 0;
            for (UIComponent child : component.getChildren()) {
                if (!child.isVisible() || child.getBounds() == null) continue;
                maxBottom = Math.max(maxBottom, child.getBounds().bottom());
            }
            Bounds b = component.getBounds();
            if (b != null) scroll.setContentHeight(maxBottom - b.y());
        }

        for (UIComponent child : component.getChildren()) {
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