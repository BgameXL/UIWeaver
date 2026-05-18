package dev.uiweaver.fabric.client.input;

import dev.uiweaver.api.component.ButtonComponent;
import dev.uiweaver.api.component.ScrollPanelComponent;
import dev.uiweaver.api.component.TextInputComponent;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.api.view.UIViewModel;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class InputHandler {

    private final UIScreenSpec spec;
    private final Consumer<String> actionSender;
    private TextInputComponent focusedInput = null;

    public InputHandler(UIScreenSpec spec, UIViewModel viewModel, Consumer<String> actionSender) {
        this.spec = spec;
        this.actionSender = actionSender;
    }

    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = dispatchClick(spec.getRoot(), mouseX, mouseY, button);
        if (!handled) focusedInput = null;
        return handled;
    }

    public boolean onMouseScrolled(double mouseX, double mouseY, double delta) {
        return dispatchScroll(spec.getRoot(), mouseX, mouseY, delta);
    }

    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (focusedInput == null) return false;
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            return focusedInput.tryDelete();
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            focusedInput = null;
            return true;
        }
        return false;
    }

    public boolean onCharTyped(char c, int modifiers) {
        if (focusedInput == null) return false;
        return focusedInput.tryInsert(String.valueOf(c));
    }

    private boolean dispatchClick(UIComponent component, double mouseX, double mouseY, int button) {
        if (!component.isVisible() || !component.isEnabled()) return false;

        for (int i = component.getChildren().size() - 1; i >= 0; i--) {
            if (dispatchClick(component.getChildren().get(i), mouseX, mouseY, button)) return true;
        }

        Bounds bounds = component.getBounds();
        if (bounds == null || !bounds.contains((int) mouseX, (int) mouseY)) return false;

        if (button == 0) {
            if (component instanceof ButtonComponent btn && btn.getActionId() != null) {
                actionSender.accept(btn.getActionId());
                return true;
            }
            if (component instanceof TextInputComponent input) {
                focusedInput = input;
                return true;
            }
        }
        return false;
    }

    private boolean dispatchScroll(UIComponent component, double mouseX, double mouseY, double delta) {
        if (!component.isVisible()) return false;

        for (int i = component.getChildren().size() - 1; i >= 0; i--) {
            if (dispatchScroll(component.getChildren().get(i), mouseX, mouseY, delta)) return true;
        }

        Bounds bounds = component.getBounds();
        if (bounds == null || !bounds.contains((int) mouseX, (int) mouseY)) return false;

        if (component instanceof ScrollPanelComponent panel) {
            panel.scroll((int) (-delta * 10));
            return true;
        }
        return false;
    }
}