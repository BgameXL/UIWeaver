package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.layout.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractComponent implements UIComponent {

    protected final String id;
    protected boolean visible;
    protected boolean enabled;
    protected Bounds bounds;
    protected Size preferredSize;
    protected final List<UIComponent> children = new ArrayList<>();

    protected AbstractComponent(String id, boolean visible, boolean enabled, Size preferredSize) {
        this.id = id;
        this.visible = visible;
        this.enabled = enabled;
        this.preferredSize = preferredSize != null ? preferredSize : Size.WRAP;
    }

    @Override public String getId() { return id; }
    @Override public Bounds getBounds() { return bounds; }
    @Override public void setBounds(Bounds bounds) { this.bounds = bounds; }
    @Override public boolean isVisible() { return visible; }
    @Override public boolean isEnabled() { return enabled; }
    @Override public List<UIComponent> getChildren() { return Collections.unmodifiableList(children); }
    public Size getPreferredSize() { return preferredSize; }
}