package dev.uiweaver.fabric.client.render;

import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.client.render.WidgetRenderer;
import net.minecraft.client.gui.GuiGraphics;

public interface FabricWidgetRenderer<T extends UIComponent> extends WidgetRenderer<T, GuiGraphics> {}