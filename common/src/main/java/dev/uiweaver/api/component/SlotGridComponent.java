package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;
import dev.uiweaver.api.slot.SlotDescriptor;

import java.util.List;

public class SlotGridComponent extends AbstractComponent implements Measurable {

    private static final int SLOT_SIZE = 18;

    private final List<SlotDescriptor> slots;
    private final int columns;
    private final String bindingName;

    public SlotGridComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                             List<SlotDescriptor> slots, int columns, String bindingName) {
        super(id, visible, enabled, preferredSize);
        this.slots       = List.copyOf(slots);
        this.columns     = columns;
        this.bindingName = bindingName;
    }

    @Override public ComponentType getType() { return ComponentType.SLOT_GRID; }

    @Override
    public Size measure(int availableWidth, int availableHeight) {
        return Size.fixed(columns * SLOT_SIZE, getRows() * SLOT_SIZE);
    }

    public List<SlotDescriptor> getSlots() { return slots; }
    public int getColumns()                { return columns; }
    public int getRows()                   { return (int) Math.ceil((double) slots.size() / columns); }
    public String getBindingName()         { return bindingName; }
    public boolean hasBoundContainer()     { return bindingName != null; }
}