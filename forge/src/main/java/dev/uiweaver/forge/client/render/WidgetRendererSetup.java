package dev.uiweaver.forge.client.render;

import dev.uiweaver.api.component.ComponentType;
import dev.uiweaver.forge.client.render.widget.*;

public class WidgetRendererSetup {

    public static void init() {
        ForgeWidgetRendererRegistry reg = ForgeWidgetRendererRegistry.instance();
        reg.register(ComponentType.PANEL,            new PanelRenderer());
        reg.register(ComponentType.LABEL,            new LabelRenderer());
        reg.register(ComponentType.BUTTON,           new ButtonRenderer());
        reg.register(ComponentType.ENERGY_BAR,       new EnergyBarRenderer());
        reg.register(ComponentType.FLUID_BAR,        new FluidBarRenderer());
        reg.register(ComponentType.PROGRESS_BAR,     new ProgressBarRenderer());
        reg.register(ComponentType.SLOT_GRID,        new SlotGridRenderer());
        reg.register(ComponentType.PLAYER_INVENTORY, new PlayerInventoryRenderer());
        reg.register(ComponentType.TEXT_INPUT,       new TextInputRenderer());
        reg.register(ComponentType.SCROLL_PANEL,     new ScrollPanelRenderer());
        reg.register(ComponentType.TABS,             new TabsRenderer());
        reg.register(ComponentType.CHECKBOX,         new CheckboxRenderer());
        reg.register(ComponentType.TOGGLE,           new ToggleRenderer());
        reg.register(ComponentType.SLIDER,           new SliderRenderer());
    }
}