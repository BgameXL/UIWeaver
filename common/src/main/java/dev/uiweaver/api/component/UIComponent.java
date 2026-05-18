package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Bounds;

import java.util.List;

public interface UIComponent {

    String getId();

    Bounds getBounds();

    void setBounds(Bounds bounds);

    boolean isVisible();

    boolean isEnabled();

    List<UIComponent> getChildren();

    ComponentType getType();
}
