package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;

@SuppressWarnings("unchecked")
public abstract class ComponentBuilder<T extends ComponentBuilder<T>> {

    protected String id;
    protected boolean visible = true;
    protected boolean enabled = true;
    protected Size preferredSize = Size.WRAP;

    public T id(String id)               { this.id = id;          return (T) this; }
    public T visible(boolean v)          { this.visible = v;      return (T) this; }
    public T enabled(boolean e)          { this.enabled = e;      return (T) this; }

    public T size(int width, int height) { preferredSize = Size.fixed(width, height);   return (T) this; }
    public T width(int width)            { preferredSize = Size.fixed(width, preferredSize.height()); return (T) this; }
    public T height(int height)          { preferredSize = Size.fixed(preferredSize.width(), height); return (T) this; }

    public T fillWidth()                 { preferredSize = Size.fillWidth(preferredSize.height());  return (T) this; }
    public T fillHeight()                { preferredSize = Size.fillHeight(preferredSize.width());  return (T) this; }
    public T fill()                      { preferredSize = Size.FILL;                               return (T) this; }

    public T wrapWidth()                 { preferredSize = Size.wrapWidth(preferredSize.height());  return (T) this; }
    public T wrapHeight()                { preferredSize = Size.wrapHeight(preferredSize.width());  return (T) this; }
    public T wrap()                      { preferredSize = Size.WRAP;                               return (T) this; }

    public T minSize(int w, int h)       { preferredSize = preferredSize.minWidth(w).minHeight(h); return (T) this; }
    public T maxSize(int w, int h)       { preferredSize = preferredSize.maxWidth(w).maxHeight(h); return (T) this; }
    public T minWidth(int w)             { preferredSize = preferredSize.minWidth(w);              return (T) this; }
    public T minHeight(int h)            { preferredSize = preferredSize.minHeight(h);             return (T) this; }
    public T maxWidth(int w)             { preferredSize = preferredSize.maxWidth(w);              return (T) this; }
    public T maxHeight(int h)            { preferredSize = preferredSize.maxHeight(h);             return (T) this; }
    public T grow()                      { preferredSize = preferredSize.grow();                   return (T) this; }
    public T grow(int weight)            { preferredSize = preferredSize.grow(weight);             return (T) this; }

    public abstract UIComponent build();
}