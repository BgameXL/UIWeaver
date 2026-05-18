package dev.uiweaver.client.input;

import dev.uiweaver.api.component.TextInputComponent;

public class FocusManager {

    private static TextInputComponent focused = null;

    public static void focus(TextInputComponent component) {
        focused = component;
    }

    public static void clearFocus() {
        focused = null;
    }

    public static TextInputComponent getFocused() {
        return focused;
    }

    public static boolean isFocused(TextInputComponent component) {
        return focused == component;
    }

    public static boolean hasFocus() {
        return focused != null;
    }
}