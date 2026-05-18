package dev.uiweaver.api.debug;

import dev.uiweaver.api.component.UIComponent;

import java.util.List;

public class ComponentTreeBuilder {

    public static ComponentTreeNode snapshot(UIComponent component) {
        List<ComponentTreeNode> children = component.getChildren().stream()
                .map(ComponentTreeBuilder::snapshot)
                .toList();

        return new ComponentTreeNode(
                component.getType().name(),
                component.getId(),
                component.getBounds(),
                component.isVisible(),
                component.isEnabled(),
                children
        );
    }
}