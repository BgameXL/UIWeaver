package dev.uiweaver.api.component;

public class ProgressBarBuilder extends ComponentBuilder<ProgressBarBuilder> {

    private ProgressBarComponent.Direction direction = ProgressBarComponent.Direction.LEFT_TO_RIGHT;

    public ProgressBarBuilder direction(ProgressBarComponent.Direction direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public ProgressBarComponent build() {
        return new ProgressBarComponent(id, visible, enabled, preferredSize, direction);
    }
}