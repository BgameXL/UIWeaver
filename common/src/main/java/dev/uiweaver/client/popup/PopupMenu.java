package dev.uiweaver.client.popup;

import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class PopupMenu {

    private final List<ContextMenuItem> items;
    private final int anchorX;
    private final int anchorY;
    private boolean closed = false;

    public PopupMenu(List<ContextMenuItem> items, int anchorX, int anchorY) {
        this.items   = List.copyOf(items);
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public List<ContextMenuItem> getItems() { return items; }
    public int getAnchorX()                 { return anchorX; }
    public int getAnchorY()                 { return anchorY; }
    public void close()                     { closed = true; }
    public boolean isClosed()               { return closed; }
}