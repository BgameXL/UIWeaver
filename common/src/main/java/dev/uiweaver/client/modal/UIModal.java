package dev.uiweaver.client.modal;

import net.minecraft.client.gui.GuiGraphics;

public abstract class UIModal {

    private boolean closed = false;

    public abstract void render(GuiGraphics graphics, int screenW, int screenH, int mouseX, int mouseY);

    public boolean onMouseClicked(double mouseX, double mouseY, int button) { return true; }
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers)   { return true; }
    public boolean onCharTyped(char c, int modifiers)                       { return true; }

    public void close() { closed = true; }
    public boolean isClosed() { return closed; }
}