package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;
import net.minecraft.network.chat.Component;

public class ToggleComponent extends AbstractComponent implements Measurable {

    private static final int HEIGHT   = 14;
    private static final int MIN_WIDTH = 36;

    private boolean on;
    private final Component labelOn;
    private final Component labelOff;
    private final String actionId;

    public ToggleComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                           Component labelOn, Component labelOff, boolean on, String actionId) {
        super(id, visible, enabled, preferredSize);
        this.labelOn  = labelOn;
        this.labelOff = labelOff;
        this.on       = on;
        this.actionId = actionId;
    }

    @Override public ComponentType getType() { return ComponentType.TOGGLE; }

    @Override
    public Size measure(int availableWidth, int availableHeight) {
        int textW = Math.max(MeasureProvider.textWidth(labelOn.getString()),
                             MeasureProvider.textWidth(labelOff.getString()));
        return Size.fixed(Math.max(MIN_WIDTH, textW + 16), HEIGHT);
    }

    public void toggle()           { this.on = !this.on; }
    public boolean isOn()          { return on; }
    public void setOn(boolean v)   { this.on = v; }
    public Component getLabelOn()  { return labelOn; }
    public Component getLabelOff() { return labelOff; }
    public Component getActiveLabel() { return on ? labelOn : labelOff; }
    public String getActionId()    { return actionId; }
}