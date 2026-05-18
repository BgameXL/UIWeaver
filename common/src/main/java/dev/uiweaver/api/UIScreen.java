package dev.uiweaver.api;

import dev.uiweaver.api.spec.UIScreenSpec;

@FunctionalInterface
public interface UIScreen {

    UIScreenSpec build(UIBuilder ui);
}
