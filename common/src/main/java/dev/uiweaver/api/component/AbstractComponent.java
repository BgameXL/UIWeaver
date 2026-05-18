package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.layout.Size;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractComponent implements UIComponent, Tooltipable {

    protected final String id;
    protected boolean visible;
    protected boolean enabled;
    protected Bounds bounds;
    protected Size preferredSize;
    protected List<Component> tooltip = List.of();
    protected final List<UIComponent> children = new ArrayList<>();

    protected AbstractComponent(String id, boolean visible, boolean enabled, Size preferredSize) {
        this.id            = id;
        this.visible       = visible;
        this.enabled       = enabled;
        this.preferredSize = preferredSize != null ? preferredSize : Size.WRAP;
    }

    @Override public String getId()      { return id; }
    @Override public Bounds getBounds()  { return bounds; }
    @Override public void setBounds(Bounds bounds) { this.bounds = bounds; }
    @Override public boolean isVisible() { return visible; }
    @Override public boolean isEnabled() { return enabled; }
    @Override public List<UIComponent> getChildren() { return Collections.unmodifiableList(children); }
    @Override public List<Component> getTooltip()    { return tooltip; }

    public void setTooltip(List<Component> tooltip) { this.tooltip = tooltip; }
    public Size getPreferredSize() { return preferredSize; }
}