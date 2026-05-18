package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.ColumnLayout;
import dev.uiweaver.api.layout.LayoutEngine;

public class ColumnBuilder extends PanelBuilder<ColumnBuilder> {

    @Override
    protected LayoutEngine createLayout() {
        return new ColumnLayout(padding, gap, crossAxis);
    }
}