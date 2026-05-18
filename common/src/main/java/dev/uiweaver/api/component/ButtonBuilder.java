package dev.uiweaver.api.component;

import net.minecraft.network.chat.Component;

public class ButtonBuilder extends ComponentBuilder<ButtonBuilder> {

    private final Component label;
    private String actionId;

    public ButtonBuilder(Component label) { this.label = label; }

    public ButtonBuilder action(String actionId) { this.actionId = actionId; return this; }

    @Override
    public ButtonComponent build() {
        return new ButtonComponent(id, visible, enabled, preferredSize, label, actionId);
    }
}