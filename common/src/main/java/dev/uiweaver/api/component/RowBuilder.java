package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.LayoutEngine;
import dev.uiweaver.api.layout.RowLayout;

public class RowBuilder extends PanelBuilder<RowBuilder> {

    @Override
    protected LayoutEngine createLayout() {
        return new RowLayout(padding, gap, crossAxis);
    }
}