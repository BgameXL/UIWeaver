package dev.uiweaver.client.popup;

import net.minecraft.network.chat.Component;

public class ContextMenuItem {

    public static final ContextMenuItem SEPARATOR = new ContextMenuItem(Component.literal("---"), null, true);

    private final Component label;
    private final Runnable action;
    private final boolean separator;
    private boolean enabled = true;

    public ContextMenuItem(Component label, Runnable action) {
        this(label, action, false);
    }

    public ContextMenuItem(Component label, Runnable action, boolean separator) {
        this.label     = label;
        this.action    = action;
        this.separator = separator;
    }

    public static ContextMenuItem of(String label, Runnable action) {
        return new ContextMenuItem(Component.literal(label), action);
    }

    public static ContextMenuItem of(Component label, Runnable action) {
        return new ContextMenuItem(label, action);
    }

    public static ContextMenuItem separator() { return SEPARATOR; }

    public ContextMenuItem disabled() { this.enabled = false; return this; }

    public Component getLabel()  { return label; }
    public Runnable getAction()  { return action; }
    public boolean isSeparator() { return separator; }
    public boolean isEnabled()   { return enabled; }
}