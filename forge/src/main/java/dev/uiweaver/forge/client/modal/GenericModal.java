package dev.uiweaver.forge.client.modal;

import dev.uiweaver.client.modal.UIModal;
import dev.uiweaver.client.theme.ThemeRegistry;
import dev.uiweaver.client.theme.UITheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class GenericModal extends UIModal {

    public record ModalButton(Component label, Runnable action, boolean primary) {}

    private static final int PAD          = 12;
    private static final int BTN_H        = 20;
    private static final int BTN_GAP      = 6;
    private static final int MIN_W        = 160;
    private static final int LINE_H       = 11;
    private static final int TITLE_BOTTOM = 8;

    private final Component title;
    private final List<Component> lines;
    private final List<ModalButton> buttons;
    private int mx, my, mw, mh;

    public GenericModal(Component title, List<Component> lines, List<ModalButton> buttons) {
        this.title   = title;
        this.lines   = List.copyOf(lines);
        this.buttons = List.copyOf(buttons);
    }

    @Override
    public void render(GuiGraphics graphics, int screenW, int screenH, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        UITheme theme = ThemeRegistry.getActive();

        int contentW = MIN_W;
        for (Component line : lines) contentW = Math.max(contentW, font.width(line) + PAD * 2);
        contentW = Math.max(contentW, font.width(title) + PAD * 2 + 20);

        int totalBtnW = buttons.stream().mapToInt(b -> font.width(b.label()) + 20).sum()
                + BTN_GAP * (buttons.size() - 1);
        contentW = Math.max(contentW, totalBtnW + PAD * 2);

        int contentH = PAD
                + font.lineHeight + TITLE_BOTTOM
                + 1
                + (lines.isEmpty() ? 0 : PAD / 2)
                + lines.size() * LINE_H
                + PAD
                + BTN_H
                + PAD;

        mw = contentW;
        mh = contentH;
        mx = (screenW - mw) / 2;
        my = (screenH - mh) / 2;

        graphics.fill(mx, my, mx + mw, my + mh, theme.getBackgroundColor());
        graphics.renderOutline(mx, my, mw, mh, theme.getBorderColor());

        int y = my + PAD;

        graphics.drawString(font, title, mx + PAD, y, theme.getTextColor(), false);
        y += font.lineHeight + TITLE_BOTTOM / 2;

        graphics.fill(mx + PAD, y, mx + mw - PAD, y + 1, theme.getBorderColor());
        y += 1 + PAD / 2;


        for (Component line : lines) {
            graphics.drawString(font, line, mx + PAD, y, theme.getTextColorDisabled(), false);
            y += LINE_H;
        }

        y = my + mh - PAD - BTN_H;
        int x = mx + mw - PAD;

        for (int i = buttons.size() - 1; i >= 0; i--) {
            ModalButton btn = buttons.get(i);
            int btnW = font.width(btn.label()) + 20;
            x -= btnW;

            boolean hovered = mouseX >= x && mouseX < x + btnW && mouseY >= y && mouseY < y + BTN_H;
            int bgColor = btn.primary()
                    ? (hovered ? 0xFF3A6FCC : 0xFF2A5FBC)
                    : (hovered ? theme.getButtonHoverColor() : theme.getButtonColor());

            graphics.fill(x, y, x + btnW, y + BTN_H, bgColor);
            graphics.renderOutline(x, y, btnW, BTN_H, theme.getBorderColor());

            int textX = x + (btnW - font.width(btn.label())) / 2;
            int textY = y + (BTN_H - font.lineHeight) / 2;
            graphics.drawString(font, btn.label(), textX, textY, theme.getTextColor(), false);

            x -= BTN_GAP;
        }
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return true;

        Font font = Minecraft.getInstance().font;
        int y  = my + mh - PAD - BTN_H;
        int x  = mx + mw - PAD;

        for (int i = buttons.size() - 1; i >= 0; i--) {
            ModalButton btn = buttons.get(i);
            int btnW = font.width(btn.label()) + 20;
            x -= btnW;

            if (mouseX >= x && mouseX < x + btnW && mouseY >= y && mouseY < y + BTN_H) {
                close();
                btn.action().run();
                return true;
            }
            x -= BTN_GAP;
        }

        if (mouseX < mx || mouseX > mx + mw || mouseY < my || mouseY > my + mh) {}
        return true;
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            close();
            if (!buttons.isEmpty()) buttons.get(buttons.size() - 1).action().run();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            buttons.stream().filter(ModalButton::primary).findFirst().ifPresent(b -> {
                close();
                b.action().run();
            });
            return true;
        }
        return true;
    }
}