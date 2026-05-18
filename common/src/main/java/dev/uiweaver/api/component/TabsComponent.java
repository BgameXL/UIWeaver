package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.layout.Size;
import dev.uiweaver.api.layout.StackLayout;
import net.minecraft.network.chat.Component;

import java.util.List;

public class TabsComponent extends AbstractComponent implements LayoutContainer {

    public static final int TAB_HEIGHT = 20;

    private final List<Component> tabLabels;
    private int activeTab = 0;
    private final StackLayout stackLayout = new StackLayout();

    public TabsComponent(String id, boolean visible, boolean enabled, Size preferredSize,
                         List<Component> tabLabels, List<UIComponent> contents) {
        super(id, visible, enabled, preferredSize);
        this.tabLabels = List.copyOf(tabLabels);
        this.children.addAll(contents);
    }

    @Override public ComponentType getType() { return ComponentType.TABS; }

    @Override
    public dev.uiweaver.api.layout.LayoutEngine getLayoutEngine() { return stackLayout; }

    @Override
    public List<UIComponent> getChildren() {
        return children;
    }

    public UIComponent getActiveContent() {
        if (activeTab < 0 || activeTab >= children.size()) return null;
        return children.get(activeTab);
    }

    public void setActiveTab(int index) {
        if (index >= 0 && index < tabLabels.size()) activeTab = index;
    }

    public int getActiveTab()              { return activeTab; }
    public List<Component> getTabLabels() { return tabLabels; }
    public int getTabCount()              { return tabLabels.size(); }

    public Bounds getContentBounds() {
        Bounds b = getBounds();
        if (b == null) return null;
        return Bounds.of(b.x(), b.y() + TAB_HEIGHT, b.width(), b.height() - TAB_HEIGHT);
    }

    public int getTabWidth() {
        Bounds b = getBounds();
        if (b == null || tabLabels.isEmpty()) return 0;
        return b.width() / tabLabels.size();
    }

    public int getTabAt(int screenX, int screenY) {
        Bounds b = getBounds();
        if (b == null) return -1;
        if (screenY < b.y() || screenY >= b.y() + TAB_HEIGHT) return -1;
        if (screenX < b.x() || screenX >= b.right()) return -1;
        int tw = getTabWidth();
        if (tw <= 0) return -1;
        return (screenX - b.x()) / tw;
    }
}