package dev.uiweaver.api.component;

public class FluidBarBuilder extends ComponentBuilder<FluidBarBuilder> {

    @Override
    public FluidBarComponent build() {
        return new FluidBarComponent(id, visible, enabled, preferredSize);
    }
}