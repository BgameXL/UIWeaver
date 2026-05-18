package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;
import net.minecraft.network.chat.Component;

public class ButtonComponent extends AbstractComponent implements Measurable {

    private static final int PAD_H = 8;
    private static final int PAD_V = 6;

    private final Component label;
    private final String actionId;

    public ButtonComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                           Component label, String actionId) {
        super(id, visible, enabled, preferredSize);
        this.label    = label;
        this.actionId = actionId;
    }

    @Override public ComponentType getType() { return ComponentType.BUTTON; }

    @Override
    public Size measure(int availableWidth, int availableHeight) {
        int w = MeasureProvider.textWidth(label.getString()) + PAD_H * 2;
        int h = MeasureProvider.lineHeight() + PAD_V * 2;
        return Size.fixed(w, h);
    }

    public Component getLabel() { return label; }
    public String getActionId() { return actionId; }
}