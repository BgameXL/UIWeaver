package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.layout.Size;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class ComponentBuilder<T extends ComponentBuilder<T>> {

    protected String id;
    protected boolean visible = true;
    protected boolean enabled = true;
    protected Size preferredSize = Size.WRAP;
    protected final List<UIComponent> children = new ArrayList<>();

    public T id(String id) {
        this.id = id;
        return (T) this;
    }

    public T visible(boolean visible) {
        this.visible = visible;
        return (T) this;
    }

    public T enabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    public T size(int width, int height) {
        this.preferredSize = Size.fixed(width, height);
        return (T) this;
    }

    public T width(int width) {
        this.preferredSize = Size.fixed(width, preferredSize.height());
        return (T) this;
    }

    public T height(int height) {
        this.preferredSize = Size.fixed(preferredSize.width(), height);
        return (T) this;
    }

    public abstract UIComponent build();
}
