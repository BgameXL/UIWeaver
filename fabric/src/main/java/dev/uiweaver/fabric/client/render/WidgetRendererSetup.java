package dev.uiweaver.fabric.client.render;

import dev.uiweaver.api.component.ComponentType;
import dev.uiweaver.fabric.client.render.widget.*;

public class WidgetRendererSetup {

    public static void init() {
        FabricWidgetRendererRegistry reg = FabricWidgetRendererRegistry.instance();
        reg.register(ComponentType.LABEL,        new LabelRenderer());
        reg.register(ComponentType.BUTTON,       new ButtonRenderer());
        reg.register(ComponentType.ENERGY_BAR,   new EnergyBarRenderer());
        reg.register(ComponentType.FLUID_BAR,    new FluidBarRenderer());
        reg.register(ComponentType.PROGRESS_BAR, new ProgressBarRenderer());
        reg.register(ComponentType.SLOT_GRID,    new SlotGridRenderer());
        reg.register(ComponentType.TEXT_INPUT,   new TextInputRenderer());
        reg.register(ComponentType.SCROLL_PANEL, new ScrollPanelRenderer());
    }
}