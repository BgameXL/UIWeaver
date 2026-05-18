package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;
import net.minecraft.network.chat.Component;

import java.util.List;

public class DropdownComponent extends AbstractComponent implements Measurable {

    private static final int PAD_H   = 6;
    private static final int PAD_V   = 4;
    private static final int ARROW_W = 12;

    private final List<Component> options;
    private int selectedIndex;
    private final String actionId;

    public DropdownComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                             List<Component> options, int selectedIndex, String actionId) {
        super(id, visible, enabled, preferredSize);
        this.options       = List.copyOf(options);
        this.selectedIndex = Math.max(0, Math.min(selectedIndex, options.size() - 1));
        this.actionId      = actionId;
    }

    @Override public ComponentType getType() { return ComponentType.DROPDOWN; }

    @Override
    public Size measure(int availableWidth, int availableHeight) {
        int maxW = options.stream()
                .mapToInt(o -> MeasureProvider.textWidth(o.getString()))
                .max().orElse(40);
        return Size.fixed(maxW + PAD_H * 2 + ARROW_W, MeasureProvider.lineHeight() + PAD_V * 2);
    }

    public List<Component> getOptions()    { return options; }
    public int getSelectedIndex()          { return selectedIndex; }
    public void setSelectedIndex(int i)    { this.selectedIndex = Math.max(0, Math.min(i, options.size() - 1)); }
    public Component getSelectedOption()   { return options.isEmpty() ? Component.empty() : options.get(selectedIndex); }
    public String getActionId()            { return actionId; }
    public int getArrowWidth()             { return ARROW_W; }
}