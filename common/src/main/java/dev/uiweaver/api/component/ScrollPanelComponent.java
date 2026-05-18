package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.LayoutEngine;
import dev.uiweaver.api.layout.Size;
import dev.uiweaver.client.render.LayoutAware;

import java.util.List;

public class ScrollPanelComponent extends AbstractComponent implements LayoutAware {

    private final LayoutEngine layoutEngine;
    private int scrollOffset = 0;
    private int contentHeight = 0;

    public ScrollPanelComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                                LayoutEngine layoutEngine, List<UIComponent> children) {
        super(id, visible, enabled, preferredSize);
        this.layoutEngine = layoutEngine;
        this.children.addAll(children);
    }

    @Override public ComponentType getType() { return ComponentType.SCROLL_PANEL; }
    @Override public LayoutEngine getLayoutEngine() { return layoutEngine; }

    public int getScrollOffset() { return scrollOffset; }

    public void scroll(int delta) {
        int viewHeight = getBounds() != null ? getBounds().height() : 0;
        int maxScroll = Math.max(0, contentHeight - viewHeight);
        scrollOffset = Math.max(0, Math.min(scrollOffset + delta, maxScroll));
    }

    public void setContentHeight(int height) { this.contentHeight = height; }
    public int getContentHeight() { return contentHeight; }
    public boolean canScroll() { return getBounds() != null && contentHeight > getBounds().height(); }
}