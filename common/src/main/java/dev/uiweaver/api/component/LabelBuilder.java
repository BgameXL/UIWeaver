package dev.uiweaver.api.component;

import net.minecraft.network.chat.Component;

public class LabelBuilder extends ComponentBuilder<LabelBuilder> {

    private Component text;
    private int maxWidth = Integer.MAX_VALUE;
    private boolean ellipsis = false;

    public LabelBuilder(Component text) { this.text = text; }

    public LabelBuilder text(Component text) { this.text = text; return this; }
    public LabelBuilder maxWidth(int w) { this.maxWidth = w; return this; }
    public LabelBuilder ellipsis() { this.ellipsis = true; return this; }

    @Override
    public LabelComponent build() {
        return new LabelComponent(id, visible, enabled, preferredSize, text, maxWidth, ellipsis);
    }
}