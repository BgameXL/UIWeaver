package dev.uiweaver.api.component;

import net.minecraft.network.chat.Component;

public class ToggleBuilder extends ComponentBuilder<ToggleBuilder> {

    private Component labelOn  = Component.literal("ON");
    private Component labelOff = Component.literal("OFF");
    private boolean on         = false;
    private String actionId    = null;

    public ToggleBuilder labels(String on, String off) {
        this.labelOn  = Component.literal(on);
        this.labelOff = Component.literal(off);
        return this;
    }

    public ToggleBuilder labels(Component on, Component off) {
        this.labelOn  = on;
        this.labelOff = off;
        return this;
    }

    public ToggleBuilder on(boolean v)           { this.on       = v;      return this; }
    public ToggleBuilder action(String actionId) { this.actionId = actionId; return this; }

    @Override
    public ToggleComponent build() {
        return applyTooltip(new ToggleComponent(id, visible, enabled, preferredSize,
                labelOn, labelOff, on, actionId));
    }
}