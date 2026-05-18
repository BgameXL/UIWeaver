package dev.uiweaver.forge.client.debug;

import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.layout.Bounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import dev.uiweaver.api.component.TabsComponent;

public class UIDebugOverlay {

    private static boolean enabled = false;

    public static void toggle() { enabled = !enabled; }
    public static boolean isEnabled() { return enabled; }

    public static void render(GuiGraphics graphics, UIComponent root) {
        if (!enabled) return;
        renderComponent(graphics, root, 0);
    }

    private static void renderComponent(GuiGraphics graphics, UIComponent component, int depth) {
        if (component == null || !component.isVisible()) return;

        Bounds b = component.getBounds();
        if (b == null) return;

        int color = depthColor(depth);
        graphics.hLine(b.x(), b.right() - 1, b.y(), color);
        graphics.hLine(b.x(), b.right() - 1, b.bottom() - 1, color);
        graphics.vLine(b.x(), b.y(), b.bottom() - 1, color);
        graphics.vLine(b.right() - 1, b.y(), b.bottom() - 1, color);

        if (component.getId() != null) {
            graphics.drawString(Minecraft.getInstance().font, component.getId(), b.x() + 1, b.y() + 1, color, false);
        }

        if (component instanceof TabsComponent tabs) {
            UIComponent active = tabs.getActiveContent();
            if (active != null && active.isVisible()) {
                renderComponent(graphics, active, depth + 1);
            }
            return;
        }

        for (UIComponent child : component.getChildren()) {
            renderComponent(graphics, child, depth + 1);
        }
    }

    private static int depthColor(int depth) {
        return switch (depth % 4) {
            case 0 -> 0xFFFF4444;
            case 1 -> 0xFF44FF44;
            case 2 -> 0xFF4444FF;
            default -> 0xFFFFFF44;
        };
    }
}