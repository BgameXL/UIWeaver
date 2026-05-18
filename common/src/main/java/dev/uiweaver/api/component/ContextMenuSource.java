package dev.uiweaver.api.component;

import dev.uiweaver.client.popup.ContextMenuItem;

import java.util.List;

public interface ContextMenuSource {

    /**
     * Returns the items to show in the context menu on right-click.
     * Return an empty list to disable context menu for this component.
     */
    List<ContextMenuItem> getContextMenuItems();
}