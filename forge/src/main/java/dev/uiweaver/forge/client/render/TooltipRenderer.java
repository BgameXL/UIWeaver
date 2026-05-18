package dev.uiweaver.forge.client.render;

import dev.uiweaver.api.component.Tooltipable;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.layout.Bounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class TooltipRenderer {

    public static void renderTooltips(GuiGraphics graphics, UIComponent root, int mouseX, int mouseY) {
        renderTree(graphics, root, mouseX, mouseY);
    }

    private static boolean renderTree(GuiGraphics graphics, UIComponent component, int mouseX, int mouseY) {
        if (!component.isVisible()) return false;

        for (int i = component.getChildren().size() - 1; i >= 0; i--) {
            if (renderTree(graphics, component.getChildren().get(i), mouseX, mouseY)) return true;
        }

        if (!(component instanceof Tooltipable t)) return false;
        List<Component> lines = t.getTooltip();
        if (lines.isEmpty()) return false;

        Bounds b = component.getBounds();
        if (b == null || !b.contains(mouseX, mouseY)) return false;

        List<FormattedCharSequence> sequences = lines.stream()
                .map(Component::getVisualOrderText)
                .toList();
        graphics.renderTooltip(Minecraft.getInstance().font, sequences, mouseX, mouseY);
        return true;
    }
}