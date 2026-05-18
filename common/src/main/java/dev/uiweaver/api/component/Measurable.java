package dev.uiweaver.api.component;

import dev.uiweaver.api.layout.Size;

public interface Measurable {

    Size measure(int availableWidth, int availableHeight);
}