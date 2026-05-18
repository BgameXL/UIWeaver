package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;
import net.minecraft.network.chat.Component;

import java.util.List;

@SuppressWarnings("unchecked")
public abstract class ComponentBuilder<T extends ComponentBuilder<T>> {

    protected String id;
    protected boolean visible  = true;
    protected boolean enabled  = true;
    protected Size preferredSize = Size.WRAP;
    protected List<Component> tooltip = List.of();

    public T id(String id)      { this.id = id;      return (T) this; }
    public T visible(boolean v) { this.visible = v;  return (T) this; }
    public T enabled(boolean e) { this.enabled = e;  return (T) this; }

    public T tooltip(String text)        { this.tooltip = List.of(Component.literal(text)); return (T) this; }
    public T tooltip(Component... lines) { this.tooltip = List.of(lines);                   return (T) this; }
    public T tooltip(List<Component> l)  { this.tooltip = List.copyOf(l);                   return (T) this; }

    public T size(int width, int height) { preferredSize = Size.fixed(width, height);              return (T) this; }
    public T width(int width)            { preferredSize = preferredSize.fixedWidth(width);         return (T) this; }
    public T height(int height)          { preferredSize = preferredSize.fixedHeight(height);       return (T) this; }

    public T fillWidth()  { preferredSize = Size.fillWidth(preferredSize.height());  return (T) this; }
    public T fillHeight() { preferredSize = Size.fillHeight(preferredSize.width());  return (T) this; }
    public T fill()       { preferredSize = Size.FILL;                               return (T) this; }

    public T wrapWidth()  { preferredSize = Size.wrapWidth(preferredSize.height());  return (T) this; }
    public T wrapHeight() { preferredSize = Size.wrapHeight(preferredSize.width());  return (T) this; }
    public T wrap()       { preferredSize = Size.WRAP;                               return (T) this; }

    public T minSize(int w, int h) { preferredSize = preferredSize.minWidth(w).minHeight(h); return (T) this; }
    public T maxSize(int w, int h) { preferredSize = preferredSize.maxWidth(w).maxHeight(h); return (T) this; }
    public T minWidth(int w)       { preferredSize = preferredSize.minWidth(w);              return (T) this; }
    public T minHeight(int h)      { preferredSize = preferredSize.minHeight(h);             return (T) this; }
    public T maxWidth(int w)       { preferredSize = preferredSize.maxWidth(w);              return (T) this; }
    public T maxHeight(int h)      { preferredSize = preferredSize.maxHeight(h);             return (T) this; }
    public T grow()                { preferredSize = preferredSize.grow();                   return (T) this; }
    public T grow(int weight)      { preferredSize = preferredSize.grow(weight);             return (T) this; }

    public abstract UIComponent build();

    protected <C extends AbstractComponent> C applyTooltip(C component) {
        if (!tooltip.isEmpty()) component.setTooltip(tooltip);
        return component;
    }
}