package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;
import dev.uiweaver.api.slot.SlotDescriptor;

import java.util.ArrayList;
import java.util.List;

public class SlotGridBuilder extends ComponentBuilder<SlotGridBuilder> {

    private static final int SLOT_SIZE = 18;

    private final int columns;
    private final int rows;
    private SlotDescriptor.Role role = SlotDescriptor.Role.INPUT;

    public SlotGridBuilder(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        this.preferredSize = Size.fixed(columns * SLOT_SIZE, rows * SLOT_SIZE);
    }

    public SlotGridBuilder role(SlotDescriptor.Role role) { this.role = role; return this; }

    @Override
    public SlotGridComponent build() {
        List<SlotDescriptor> slots = new ArrayList<>();
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                slots.add(SlotDescriptor.builder(index++, col * SLOT_SIZE + 1, row * SLOT_SIZE + 1)
                        .role(role)
                        .build());
            }
        }
        return new SlotGridComponent(id, visible, enabled, preferredSize, slots, columns);
    }
}