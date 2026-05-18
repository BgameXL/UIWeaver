package dev.uiweaver.forge.client.input;

import dev.uiweaver.api.component.ButtonComponent;
import dev.uiweaver.forge.client.modal.Modals;
import dev.uiweaver.api.component.ScrollPanelComponent;
import dev.uiweaver.api.component.CheckboxComponent;
import dev.uiweaver.api.component.SliderComponent;
import dev.uiweaver.api.component.TabsComponent;
import dev.uiweaver.api.component.ToggleComponent;
import dev.uiweaver.api.component.TextInputComponent;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.layout.Bounds;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.input.FocusManager;
import dev.uiweaver.forge.client.screen.ForgeScreenActionSender;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class InputHandler {

    private SliderComponent draggingSlider = null;
    private final UIScreenSpec spec;
    private final Consumer<String> actionSender;

    public InputHandler(UIScreenSpec spec, UIViewModel viewModel, Consumer<String> actionSender) {
        this.spec         = spec;
        this.actionSender = actionSender;
    }

    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = dispatchClick(spec.getRoot(), mouseX, mouseY, button, 0);
        if (!handled) FocusManager.clearFocus();
        return handled;
    }

    public boolean onMouseScrolled(double mouseX, double mouseY, double delta) {
        return dispatchScroll(spec.getRoot(), mouseX, mouseY, delta);
    }

    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        TextInputComponent input = FocusManager.getFocused();
        if (input == null) return false;

        boolean ctrl  = (modifiers & GLFW.GLFW_MOD_CONTROL) != 0;
        boolean shift = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;

        if (ctrl) {
            return switch (keyCode) {
                case GLFW.GLFW_KEY_A -> { input.selectAll(); yield true; }
                case GLFW.GLFW_KEY_C -> { setClipboard(input.getSelectedText()); yield true; }
                case GLFW.GLFW_KEY_X -> { setClipboard(input.cut()); fireChange(input); yield true; }
                case GLFW.GLFW_KEY_V -> { boolean ok = input.insert(getClipboard()); if (ok) fireChange(input); yield ok; }
                case GLFW.GLFW_KEY_LEFT  -> { input.moveCursorByWord(-1, shift); yield true; }
                case GLFW.GLFW_KEY_RIGHT -> { input.moveCursorByWord(1, shift);  yield true; }
                default -> false;
            };
        }

        return switch (keyCode) {
            case GLFW.GLFW_KEY_BACKSPACE -> { boolean ok = input.deleteBackward(); if (ok) fireChange(input); yield ok; }
            case GLFW.GLFW_KEY_DELETE    -> { boolean ok = input.deleteForward();  if (ok) fireChange(input); yield ok; }
            case GLFW.GLFW_KEY_LEFT      -> { input.moveCursorBy(-1, shift); yield true; }
            case GLFW.GLFW_KEY_RIGHT     -> { input.moveCursorBy(1, shift);  yield true; }
            case GLFW.GLFW_KEY_HOME      -> { input.moveCursorToStart(shift); yield true; }
            case GLFW.GLFW_KEY_END       -> { input.moveCursorToEnd(shift);   yield true; }
            case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                fireSubmit(input);
                FocusManager.clearFocus();
                yield true;
            }
            case GLFW.GLFW_KEY_ESCAPE -> { FocusManager.clearFocus(); yield true; }
            default -> false;
        };
    }

    public boolean onCharTyped(char c, int modifiers) {
        TextInputComponent input = FocusManager.getFocused();
        if (input == null) return false;
        boolean ok = input.insert(String.valueOf(c));
        if (ok) fireChange(input);
        return ok;
    }

    private boolean dispatchClick(UIComponent component, double mouseX, double mouseY,
                                  int button, int scrollOffsetY) {
        if (!component.isVisible() || !component.isEnabled()) return false;

        int childOffset = scrollOffsetY;
        if (component instanceof ScrollPanelComponent panel) {
            childOffset += panel.getScrollOffset();
        }

        for (int i = component.getChildren().size() - 1; i >= 0; i--) {
            if (dispatchClick(component.getChildren().get(i), mouseX, mouseY, button, childOffset)) return true;
        }

        Bounds bounds = component.getBounds();
        if (bounds == null) return false;

        double adjustedY = mouseY + scrollOffsetY;
        if (!bounds.contains((int) mouseX, (int) adjustedY)) return false;

        if (button == 0) {
            if (component instanceof ButtonComponent btn && btn.getActionId() != null) {
                if (btn.hasConfirm()) {
                    Modals.confirm(btn.getConfirmTitle(), btn.getConfirmMessage(),
                            () -> actionSender.accept(btn.getActionId()));
                } else {
                    actionSender.accept(btn.getActionId());
                }
                return true;
            }
            if (component instanceof TabsComponent tabs) {
                int tabIdx = tabs.getTabAt((int) mouseX, (int) adjustedY);
                if (tabIdx >= 0) { tabs.setActiveTab(tabIdx); return true; }
            }
            if (component instanceof CheckboxComponent cb) {
                cb.toggle();
                fireAction(cb.getActionId(), cb.isChecked());
                return true;
            }
            if (component instanceof ToggleComponent tog) {
                tog.toggle();
                fireAction(tog.getActionId(), tog.isOn());
                return true;
            }
            if (component instanceof SliderComponent slider && slider.getBounds() != null) {
                slider.setValueFromX((int) mouseX, slider.getBounds().x(), slider.getBounds().width());
                slider.setDragging(true);
                draggingSlider = slider;
                fireSlider(slider);
                return true;
            }
            if (component instanceof TextInputComponent input) {
                FocusManager.focus(input);
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

    public boolean onMouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (draggingSlider == null) return false;
        Bounds b = draggingSlider.getBounds();
        if (b != null) {
            draggingSlider.setValueFromX((int) mouseX, b.x(), b.width());
            fireSlider(draggingSlider);
        }
        return true;
    }

    public boolean onMouseReleased(double mouseX, double mouseY, int button) {
        if (draggingSlider != null) {
            draggingSlider.setDragging(false);
            draggingSlider = null;
            return true;
        }
        return false;
    }

    private void fireAction(String actionId, boolean value) {
        if (actionId == null) return;
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("value", value);
        ForgeScreenActionSender.sendAction(spec.getScreenId(), actionId, tag);
    }

    private void fireSlider(SliderComponent slider) {
        if (slider.getActionId() == null) return;
        CompoundTag tag = new CompoundTag();
        tag.putInt("value", slider.getValue());
        ForgeScreenActionSender.sendAction(spec.getScreenId(), slider.getActionId(), tag);
    }

    private void fireChange(TextInputComponent input) {
        String actionId = input.getOnChangeAction();
        if (actionId == null) return;
        CompoundTag tag = new CompoundTag();
        tag.putString("value", input.getValue());
        ForgeScreenActionSender.sendAction(spec.getScreenId(), actionId, tag);
    }

    private void fireSubmit(TextInputComponent input) {
        String actionId = input.getOnSubmitAction();
        if (actionId == null) return;
        CompoundTag tag = new CompoundTag();
        tag.putString("value", input.getValue());
        ForgeScreenActionSender.sendAction(spec.getScreenId(), actionId, tag);
    }

    private static void setClipboard(String text) {
        if (!text.isEmpty()) Minecraft.getInstance().keyboardHandler.setClipboard(text);
    }

    private static String getClipboard() {
        return Minecraft.getInstance().keyboardHandler.getClipboard();
    }
}