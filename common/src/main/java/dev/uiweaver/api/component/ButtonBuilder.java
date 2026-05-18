package dev.uiweaver.api.component;

import net.minecraft.network.chat.Component;

public class ButtonBuilder extends ComponentBuilder<ButtonBuilder> {

    private final Component label;
    private String actionId;
    private String confirmTitle   = null;
    private String confirmMessage = null;

    public ButtonBuilder(Component label) { this.label = label; }

    public ButtonBuilder action(String actionId) { this.actionId = actionId; return this; }

    public ButtonBuilder confirm(String title, String message) {
        this.confirmTitle   = title;
        this.confirmMessage = message;
        return this;
    }

    @Override
    public ButtonComponent build() {
        return applyTooltip(new ButtonComponent(id, visible, enabled, preferredSize,
                label, actionId, confirmTitle, confirmMessage));
    }
}