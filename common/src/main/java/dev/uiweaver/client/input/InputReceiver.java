package dev.uiweaver.client.input;

import java.util.function.Consumer;

public interface InputReceiver {

    default boolean onMouseClicked(double mouseX, double mouseY, int button, Consumer<String> actionSender) {
        return false;
    }

    default boolean onMouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

    default boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    default boolean onCharTyped(char c, int modifiers) {
        return false;
    }
}