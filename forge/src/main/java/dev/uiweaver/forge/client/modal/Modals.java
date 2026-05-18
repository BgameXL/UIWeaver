package dev.uiweaver.forge.client.modal;

import dev.uiweaver.client.modal.ModalManager;
import net.minecraft.network.chat.Component;

import java.util.List;

public class Modals {

    /** Simple confirm dialog: message + Confirm + Cancel buttons. */
    public static void confirm(String title, String message, Runnable onConfirm) {
        confirm(Component.literal(title), Component.literal(message), onConfirm, () -> {});
    }

    public static void confirm(String title, String message, Runnable onConfirm, Runnable onCancel) {
        confirm(Component.literal(title), Component.literal(message), onConfirm, onCancel);
    }

    public static void confirm(Component title, Component message, Runnable onConfirm, Runnable onCancel) {
        ModalManager.push(new GenericModal(
                title,
                List.of(message),
                List.of(
                        new GenericModal.ModalButton(Component.literal("Confirm"), onConfirm, true),
                        new GenericModal.ModalButton(Component.literal("Cancel"),  onCancel,  false)
                )
        ));
    }

    /** Info dialog: message + OK button. */
    public static void info(String title, String message) {
        info(Component.literal(title), Component.literal(message));
    }

    public static void info(Component title, Component message) {
        ModalManager.push(new GenericModal(
                title,
                List.of(message),
                List.of(new GenericModal.ModalButton(Component.literal("OK"), () -> {}, true))
        ));
    }

    /** Warning dialog: message + OK button, styled differently via title prefix. */
    public static void warning(String message) {
        info(Component.literal("⚠ Warning"), Component.literal(message));
    }

    /** Multi-line modal with custom buttons. */
    public static void show(Component title, List<Component> lines,
                            List<GenericModal.ModalButton> buttons) {
        ModalManager.push(new GenericModal(title, lines, buttons));
    }

    /** Custom modal. */
    public static void show(dev.uiweaver.client.modal.UIModal modal) {
        ModalManager.push(modal);
    }
}