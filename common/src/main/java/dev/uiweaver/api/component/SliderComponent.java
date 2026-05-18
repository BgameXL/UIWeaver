package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;

public class SliderComponent extends AbstractComponent implements Measurable {

    private static final int HEIGHT    = 12;
    private static final int MIN_WIDTH = 60;

    private int value;
    private final int min;
    private final int max;
    private final String actionId;
    private boolean dragging = false;

    public SliderComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                           int min, int max, int value, String actionId) {
        super(id, visible, enabled, preferredSize);
        this.min      = min;
        this.max      = max;
        this.value    = Math.max(min, Math.min(max, value));
        this.actionId = actionId;
    }

    @Override public ComponentType getType() { return ComponentType.SLIDER; }

    @Override
    public Size measure(int availableWidth, int availableHeight) {
        return Size.fixed(MIN_WIDTH, HEIGHT);
    }

    public void setValueFromX(int screenX, int boundsX, int boundsWidth) {
        int trackW = boundsWidth - HEIGHT; // thumb size = HEIGHT
        int relX   = Math.max(0, Math.min(screenX - boundsX - HEIGHT / 2, trackW));
        value = min + (int) Math.round((double) relX / trackW * (max - min));
        value = Math.max(min, Math.min(max, value));
    }

    public int getThumbX(int boundsWidth) {
        int trackW = boundsWidth - HEIGHT;
        return (int) ((double)(value - min) / (max - min) * trackW) + HEIGHT / 2;
    }

    public float getFraction() {
        if (max == min) return 0f;
        return (float)(value - min) / (max - min);
    }

    public int getValue()        { return value; }
    public void setValue(int v)  { this.value = Math.max(min, Math.min(max, v)); }
    public int getMin()          { return min; }
    public int getMax()          { return max; }
    public String getActionId()  { return actionId; }
    public boolean isDragging()  { return dragging; }
    public void setDragging(boolean d) { this.dragging = d; }
}