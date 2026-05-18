package dev.uiweaver.forge.client.debug;

import dev.uiweaver.api.debug.ComponentTreeBuilder;
import dev.uiweaver.api.debug.ComponentTreeNode;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.api.component.TabsComponent;
import dev.uiweaver.api.component.UIComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class UIInspector {

    private static final int PANEL_X      = 4;
    private static final int PANEL_W      = 200;
    private static final int LINE_H       = 9;
    private static final int PADDING      = 4;
    private static final int BG           = 0xCC000000;
    private static final int BORDER       = 0xFF444444;
    private static final int COLOR_TYPE   = 0xFF888888;
    private static final int COLOR_ID     = 0xFFFFDD44;
    private static final int COLOR_BOUNDS = 0xFF44DDFF;
    private static final int COLOR_VALUE  = 0xFF88FF88;
    private static final int COLOR_HOVER  = 0x33FFFFFF;
    private static final int COLOR_WARN   = 0xFFFF4444;

    private UIComponent hoveredComponent = null;

    public void render(GuiGraphics graphics, UIScreenSpec spec, UIViewModel viewModel,
                       int mouseX, int mouseY, int screenHeight) {
        Font font = Minecraft.getInstance().font;
        UIComponent root = spec.getRoot();
        hoveredComponent = findHovered(root, mouseX, mouseY);

        var lines = new java.util.ArrayList<InspectorLine>();
        lines.add(new InspectorLine("── Component Tree ──", COLOR_ID, 0));
        collectTreeLines(root, 0, lines, mouseX, mouseY);
        lines.add(new InspectorLine("", COLOR_TYPE, 0));
        lines.add(new InspectorLine("── ViewModel ──", COLOR_ID, 0));
        collectViewModelLines(viewModel, lines);
        if (hoveredComponent != null) {
            lines.add(new InspectorLine("", COLOR_TYPE, 0));
            lines.add(new InspectorLine("── Hovered ──", COLOR_ID, 0));
            collectComponentDetail(hoveredComponent, lines);
        }

        int panelH = lines.size() * LINE_H + PADDING * 2;
        int panelY = Math.min(PANEL_X, screenHeight - panelH - PANEL_X);
        graphics.fill(PANEL_X, panelY, PANEL_X + PANEL_W, panelY + panelH, BG);
        graphics.renderOutline(PANEL_X, panelY, PANEL_W, panelH, BORDER);

        int y = panelY + PADDING;
        for (InspectorLine line : lines) {
            if (!line.text.isEmpty()) {
                String trimmed = trimToWidth(font, line.text, PANEL_W - PADDING * 2);
                graphics.drawString(font, trimmed, PANEL_X + PADDING + line.indent * 6, y, line.color, false);
            }
            y += LINE_H;
        }

        if (hoveredComponent != null && hoveredComponent.getBounds() != null) {
            Bounds b = hoveredComponent.getBounds();
            graphics.fill(b.x(), b.y(), b.right(), b.bottom(), COLOR_HOVER);
            graphics.renderOutline(b.x(), b.y(), b.width(), b.height(), COLOR_ID);
        }
    }

    private void collectTreeLines(UIComponent component, int depth,
                                  List<InspectorLine> lines, int mouseX, int mouseY) {
        if (component == null || !component.isVisible()) {
            return;
        }

        Bounds bounds = component.getBounds();
        boolean hovered = bounds != null && bounds.contains(mouseX, mouseY);

        String prefix = depth == 0 ? "" : "└ ";
        String typeStr = prefix + String.valueOf(component.getType()).toLowerCase();

        int typeColor = hovered ? COLOR_ID : COLOR_TYPE;
        lines.add(new InspectorLine(typeStr, typeColor, depth));

        if (component.getId() != null) {
            lines.add(new InspectorLine("id=" + component.getId(), COLOR_ID, depth + 1));
        }

        if (bounds != null) {
            lines.add(new InspectorLine(
                    bounds.x() + "," + bounds.y() + " " + bounds.width() + "×" + bounds.height(),
                    COLOR_BOUNDS,
                    depth + 1
            ));
        }

        if (component instanceof TabsComponent tabs) {
            UIComponent active = tabs.getActiveContent();

            if (active != null && active.isVisible()) {
                collectTreeLines(active, depth + 1, lines, mouseX, mouseY);
            }

            return;
        }

        for (UIComponent child : component.getChildren()) {
            collectTreeLines(child, depth + 1, lines, mouseX, mouseY);
        }
    }

    private void collectViewModelLines(UIViewModel viewModel, List<InspectorLine> lines) {
        var entries = viewModel.entries();
        if (entries.isEmpty()) {
            lines.add(new InspectorLine("(empty)", COLOR_TYPE, 1));
        } else {
            for (var entry : entries) {
                lines.add(new InspectorLine(entry.getKey() + " = " + entry.getValue(), COLOR_VALUE, 1));
            }
        }
    }

    private void collectComponentDetail(UIComponent component, List<InspectorLine> lines) {
        lines.add(new InspectorLine("type=" + component.getType(), COLOR_TYPE, 1));

        if (component.getId() != null) {
            lines.add(new InspectorLine("id=" + component.getId(), COLOR_ID, 1));
        }

        if (component.getBounds() != null) {
            Bounds b = component.getBounds();
            lines.add(new InspectorLine("x=" + b.x() + " y=" + b.y(), COLOR_BOUNDS, 1));
            lines.add(new InspectorLine("w=" + b.width() + " h=" + b.height(), COLOR_BOUNDS, 1));
        }

        lines.add(new InspectorLine(
                "visible=" + component.isVisible() + " enabled=" + component.isEnabled(),
                COLOR_TYPE,
                1
        ));
    }

    private UIComponent findHovered(UIComponent component, int mouseX, int mouseY) {
        if (component == null || !component.isVisible()) {
            return null;
        }

        if (component instanceof TabsComponent tabs) {
            UIComponent active = tabs.getActiveContent();

            if (active != null) {
                UIComponent found = findHovered(active, mouseX, mouseY);
                if (found != null) return found;
            }

            Bounds b = component.getBounds();
            if (b != null && b.contains(mouseX, mouseY)) {
                return component;
            }

            return null;
        }

        List<UIComponent> children = component.getChildren();

        for (int i = children.size() - 1; i >= 0; i--) {
            UIComponent found = findHovered(children.get(i), mouseX, mouseY);
            if (found != null) return found;
        }

        Bounds b = component.getBounds();
        if (b != null && b.contains(mouseX, mouseY)) {
            return component;
        }

        return null;
    }

    private String trimToWidth(Font font, String text, int maxWidth) {
        return font.plainSubstrByWidth(text, maxWidth);
    }

    private record InspectorLine(String text, int color, int indent) {}
}