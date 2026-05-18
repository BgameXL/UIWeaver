package dev.uiweaver.client.popup;

public class PopupManager {

    private static PopupMenu active = null;

    public static void open(PopupMenu menu) {
        active = menu;
    }

    public static void close() {
        if (active != null) active.close();
        active = null;
    }

    public static boolean hasPopup() {
        return active != null && !active.isClosed();
    }

    public static PopupMenu getActive() {
        if (active != null && active.isClosed()) active = null;
        return active;
    }
}