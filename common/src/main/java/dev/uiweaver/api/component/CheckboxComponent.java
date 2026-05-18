package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;
import net.minecraft.network.chat.Component;

public class CheckboxComponent extends AbstractComponent implements Measurable {

    private static final int BOX_SIZE = 11;
    private static final int GAP = 4;

    private boolean checked;
    private final Component label;
    private final String actionId;

    public CheckboxComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                             Component label, boolean checked, String actionId) {
        super(id, visible, enabled, preferredSize);
        this.label    = label;
        this.checked  = checked;
        this.actionId = actionId;
    }

    @Override public ComponentType getType() { return ComponentType.CHECKBOX; }

    @Override
    public Size measure(int availableWidth, int availableHeight) {
        int w = BOX_SIZE + GAP + MeasureProvider.textWidth(label.getString());
        int h = Math.max(BOX_SIZE, MeasureProvider.lineHeight());
        return Size.fixed(w, h);
    }

    public void toggle()             { this.checked = !this.checked; }
    public boolean isChecked()       { return checked; }
    public void setChecked(boolean v){ this.checked = v; }
    public Component getLabel()      { return label; }
    public String getActionId()      { return actionId; }
    public int getBoxSize()          { return BOX_SIZE; }
}