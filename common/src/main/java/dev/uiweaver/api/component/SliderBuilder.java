package dev.uiweaver.api.component;

public class SliderBuilder extends ComponentBuilder<SliderBuilder> {

    private int min      = 0;
    private int max      = 100;
    private int value    = 0;
    private String actionId = null;

    public SliderBuilder min(int min)            { this.min      = min;      return this; }
    public SliderBuilder max(int max)            { this.max      = max;      return this; }
    public SliderBuilder value(int value)        { this.value    = value;    return this; }
    public SliderBuilder range(int min, int max) { this.min = min; this.max = max; return this; }
    public SliderBuilder action(String actionId) { this.actionId = actionId; return this; }

    @Override
    public SliderComponent build() {
        return applyTooltip(new SliderComponent(id, visible, enabled, preferredSize,
                min, max, value, actionId));
    }
}