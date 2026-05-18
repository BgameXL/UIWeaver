package dev.uiweaver.api.component;

import dev.uiweaver.api.binding.Bindable;
import dev.uiweaver.api.layout.Size;

public class FluidBarComponent extends AbstractComponent implements Bindable {

    private long amount;
    private long capacity;
    private String fluidId = "";

    public FluidBarComponent(String id, boolean visible, boolean enabled, Size preferredSize) {
        super(id, visible, enabled, preferredSize);
    }

    @Override public ComponentType getType() { return ComponentType.FLUID_BAR; }

    @Override
    public void applyBinding(String property, Object val) {
        switch (property) {
            case "amount" -> amount = ((Number) val).longValue();
            case "capacity" -> capacity = ((Number) val).longValue();
            case "fluidId" -> fluidId = (String) val;
        }
    }

    @Override
    public boolean supportsProperty(String property) {
        return property.equals("amount") || property.equals("capacity") || property.equals("fluidId");
    }

    public float getFillFraction() {
        if (capacity <= 0) return 0f;
        return Math.min(1f, (float) amount / capacity);
    }

    public long getAmount() { return amount; }
    public long getCapacity() { return capacity; }
    public String getFluidId() { return fluidId; }
}