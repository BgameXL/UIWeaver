package dev.uiweaver.api.debug;

import dev.uiweaver.api.layout.Bounds;

import java.util.List;

public record ComponentTreeNode(
        String type,
        String id,
        Bounds bounds,
        boolean visible,
        boolean enabled,
        List<ComponentTreeNode> children
) {
    public boolean hasId() { return id != null && !id.isEmpty(); }
}