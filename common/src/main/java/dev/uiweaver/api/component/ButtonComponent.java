package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;
import net.minecraft.network.chat.Component;

public class ButtonComponent extends AbstractComponent {

    private final Component label;
    private final String actionId;

    public ButtonComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                           Component label, String actionId) {
        super(id, visible, enabled, preferredSize);
        this.label = label;
        this.actionId = actionId;
    }

    @Override public ComponentType getType() { return ComponentType.BUTTON; }

    public Component getLabel() { return label; }
    public String getActionId() { return actionId; }
}