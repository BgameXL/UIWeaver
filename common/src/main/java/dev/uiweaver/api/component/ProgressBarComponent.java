package dev.uiweaver.api.component;

import dev.uiweaver.api.binding.Bindable;
import dev.uiweaver.api.layout.Size;

public class ProgressBarComponent extends AbstractComponent implements Bindable, Measurable {

    public enum Direction { LEFT_TO_RIGHT, RIGHT_TO_LEFT, BOTTOM_TO_TOP, TOP_TO_BOTTOM }

    private long value;
    private long maxValue;
    private final Direction direction;

    public ProgressBarComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                                Direction direction) {
        super(id, visible, enabled, preferredSize);
        this.direction = direction;
    }

    @Override public ComponentType getType() { return ComponentType.PROGRESS_BAR; }

    @Override
    public Size measure(int availableWidth, int availableHeight) {
        return Size.fixed(16, 16);
    }

    @Override
    public void applyBinding(String property, Object val) {
        switch (property) {
            case "value"    -> value    = ((Number) val).longValue();
            case "maxValue" -> maxValue = ((Number) val).longValue();
        }
    }

    @Override
    public boolean supportsProperty(String property) {
        return property.equals("value") || property.equals("maxValue");
    }

    public float getFillFraction() {
        if (maxValue <= 0) return 0f;
        return Math.min(1f, (float) value / maxValue);
    }

    public long getValue()      { return value; }
    public long getMaxValue()   { return maxValue; }
    public Direction getDirection() { return direction; }
}