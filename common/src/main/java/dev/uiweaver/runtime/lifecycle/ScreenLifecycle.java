package dev.uiweaver.runtime.lifecycle;

public interface ScreenLifecycle {

    default void onOpen() {}

    default void onTick() {}

    default void onClose() {}

    default void onDispose() {}
}