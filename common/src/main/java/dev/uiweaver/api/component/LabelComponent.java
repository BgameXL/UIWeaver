package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;
import net.minecraft.network.chat.Component;

public class LabelComponent extends AbstractComponent {

    private Component text;
    private final int maxWidth;
    private final boolean ellipsis;

    public LabelComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                          Component text, int maxWidth, boolean ellipsis) {
        super(id, visible, enabled, preferredSize);
        this.text = text;
        this.maxWidth = maxWidth;
        this.ellipsis = ellipsis;
    }

    @Override public ComponentType getType() { return ComponentType.LABEL; }

    public Component getText() { return text; }
    public void setText(Component text) { this.text = text; }
    public int getMaxWidth() { return maxWidth; }
    public boolean isEllipsis() { return ellipsis; }
}