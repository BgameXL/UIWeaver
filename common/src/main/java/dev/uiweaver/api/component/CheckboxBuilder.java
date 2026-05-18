package dev.uiweaver.api.component;

import net.minecraft.network.chat.Component;

public class CheckboxBuilder extends ComponentBuilder<CheckboxBuilder> {

    private final Component label;
    private boolean checked   = false;
    private String actionId   = null;

    public CheckboxBuilder(Component label) { this.label = label; }
    public CheckboxBuilder(String label)    { this(Component.literal(label)); }

    public CheckboxBuilder checked(boolean v)    { this.checked  = v;        return this; }
    public CheckboxBuilder action(String action) { this.actionId = action;   return this; }

    @Override
    public CheckboxComponent build() {
        return applyTooltip(new CheckboxComponent(id, visible, enabled, preferredSize,
                label, checked, actionId));
    }
}