package dev.uiweaver.client.modal;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayDeque;
import java.util.Deque;

public class ModalManager {

    private static final Deque<UIModal> STACK = new ArrayDeque<>();

    public static void push(UIModal modal) {
        STACK.push(modal);
    }

    public static void clear() {
        STACK.clear();
    }

    public static boolean hasModal() {
        pruneClosedModals();
        return !STACK.isEmpty();
    }

    public static void render(GuiGraphics graphics, int screenW, int screenH, int mouseX, int mouseY) {
        pruneClosedModals();
        if (STACK.isEmpty()) return;

        graphics.fill(0, 0, screenW, screenH, 0x88000000);
        STACK.peek().render(graphics, screenW, screenH, mouseX, mouseY);
    }

    public static boolean onMouseClicked(double mouseX, double mouseY, int button) {
        pruneClosedModals();
        if (STACK.isEmpty()) return false;
        return STACK.peek().onMouseClicked(mouseX, mouseY, button);
    }

    public static boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        pruneClosedModals();
        if (STACK.isEmpty()) return false;
        return STACK.peek().onKeyPressed(keyCode, scanCode, modifiers);
    }

    public static boolean onCharTyped(char c, int modifiers) {
        pruneClosedModals();
        if (STACK.isEmpty()) return false;
        return STACK.peek().onCharTyped(c, modifiers);
    }

    private static void pruneClosedModals() {
        while (!STACK.isEmpty() && STACK.peek().isClosed()) {
            STACK.pop();
        }
    }
}