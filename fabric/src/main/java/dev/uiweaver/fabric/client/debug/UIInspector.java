package dev.uiweaver.fabric.client.debug;

import dev.uiweaver.api.debug.ComponentTreeBuilder;
import dev.uiweaver.api.debug.ComponentTreeNode;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.api.view.UIViewModel;
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

    private ComponentTreeNode hoveredNode = null;

    public void render(GuiGraphics graphics, UIScreenSpec spec, UIViewModel viewModel,
                       int mouseX, int mouseY, int screenHeight) {
        Font font = Minecraft.getInstance().font;
        ComponentTreeNode root = ComponentTreeBuilder.snapshot(spec.getRoot());
        hoveredNode = findHovered(root, mouseX, mouseY);

        // collect lines
        var lines = new java.util.ArrayList<InspectorLine>();
        lines.add(new InspectorLine("── Component Tree ──", COLOR_ID, 0));
        collectTreeLines(root, 0, lines, mouseX, mouseY);
        lines.add(new InspectorLine("", COLOR_TYPE, 0));
        lines.add(new InspectorLine("── ViewModel ──", COLOR_ID, 0));
        collectViewModelLines(viewModel, lines);
        if (hoveredNode != null) {
            lines.add(new InspectorLine("", COLOR_TYPE, 0));
            lines.add(new InspectorLine("── Hovered ──", COLOR_ID, 0));
            collectNodeDetail(hoveredNode, lines);
        }

        // draw panel
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

        if (hoveredNode != null && hoveredNode.bounds() != null) {
            Bounds b = hoveredNode.bounds();
            graphics.fill(b.x(), b.y(), b.right(), b.bottom(), COLOR_HOVER);
            graphics.renderOutline(b.x(), b.y(), b.width(), b.height(), COLOR_ID);
        }
    }

    private void collectTreeLines(ComponentTreeNode node, int depth,
                                   List<InspectorLine> lines, int mouseX, int mouseY) {
        boolean hovered = node.bounds() != null && node.bounds().contains(mouseX, mouseY);
        String prefix = depth == 0 ? "" : "└ ";
        String typeStr = prefix + node.type().toLowerCase();
        int typeColor = !node.visible() ? COLOR_WARN : hovered ? COLOR_ID : COLOR_TYPE;
        lines.add(new InspectorLine(typeStr, typeColor, depth));

        if (node.hasId()) {
            lines.add(new InspectorLine("id=" + node.id(), COLOR_ID, depth + 1));
        }
        if (node.bounds() != null) {
            Bounds b = node.bounds();
            lines.add(new InspectorLine(b.x() + "," + b.y() + " " + b.width() + "×" + b.height(),
                    COLOR_BOUNDS, depth + 1));
        }

        for (ComponentTreeNode child : node.children()) {
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

    private void collectNodeDetail(ComponentTreeNode node, List<InspectorLine> lines) {
        lines.add(new InspectorLine("type=" + node.type(), COLOR_TYPE, 1));
        if (node.hasId()) lines.add(new InspectorLine("id=" + node.id(), COLOR_ID, 1));
        if (node.bounds() != null) {
            Bounds b = node.bounds();
            lines.add(new InspectorLine("x=" + b.x() + " y=" + b.y(), COLOR_BOUNDS, 1));
            lines.add(new InspectorLine("w=" + b.width() + " h=" + b.height(), COLOR_BOUNDS, 1));
        }
        lines.add(new InspectorLine("visible=" + node.visible() + " enabled=" + node.enabled(), COLOR_TYPE, 1));
    }

    private ComponentTreeNode findHovered(ComponentTreeNode node, int mouseX, int mouseY) {
        for (int i = node.children().size() - 1; i >= 0; i--) {
            ComponentTreeNode found = findHovered(node.children().get(i), mouseX, mouseY);
            if (found != null) return found;
        }
        if (node.bounds() != null && node.bounds().contains(mouseX, mouseY)) return node;
        return null;
    }

    private String trimToWidth(Font font, String text, int maxWidth) {
        return font.plainSubstrByWidth(text, maxWidth);
    }

    private record InspectorLine(String text, int color, int indent) {}
}