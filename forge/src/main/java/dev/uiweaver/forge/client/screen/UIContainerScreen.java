package dev.uiweaver.forge.client.screen;

import dev.uiweaver.api.component.AbstractComponent;
import dev.uiweaver.api.layout.Size;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.client.input.FocusManager;
import dev.uiweaver.client.modal.ModalManager;
import dev.uiweaver.forge.client.debug.UIDebugOverlay;
import dev.uiweaver.forge.client.debug.UIInspector;
import dev.uiweaver.forge.client.input.InputHandler;
import dev.uiweaver.forge.client.render.UIRenderer;
import dev.uiweaver.runtime.menu.UIMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

public class UIContainerScreen extends AbstractContainerScreen<UIMenu> {

    private final UIScreenSpec spec;
    private final UIViewModel viewModel;
    private UIRenderer renderer;
    private InputHandler inputHandler;
    private final UIInspector inspector = new UIInspector();

    public UIContainerScreen(UIMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.spec      = menu.getSpec();
        this.viewModel = new UIViewModel();
        this.titleLabelX = -10000;
        this.titleLabelY = -10000;
        this.inventoryLabelX = -10000;
        this.inventoryLabelY = -10000;

        if (spec.getRoot() instanceof AbstractComponent ac) {
            Size pref = ac.getPreferredSize();
            if (pref.isFixedWidth())  this.imageWidth  = pref.width();
            if (pref.isFixedHeight()) this.imageHeight = pref.height();
        }
    }

    @Override
    protected void init() {
        super.init();
        this.renderer     = new UIRenderer(spec, viewModel);
        this.inputHandler = new InputHandler(spec, viewModel, this::sendAction);
        renderer.init(leftPos, topPos, imageWidth, imageHeight);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        renderer.renderBackground(graphics, mouseX, mouseY);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderer.renderForeground(graphics, mouseX, mouseY);
        renderTooltip(graphics, mouseX, mouseY);

        // Modal layer — rendered on top of everything
        ModalManager.render(graphics, width, height, mouseX, mouseY);

        if (UIDebugOverlay.isEnabled()) {
            UIDebugOverlay.render(graphics, spec.getRoot());
            inspector.render(graphics, spec, viewModel, mouseX, mouseY, height);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (ModalManager.onKeyPressed(keyCode, scanCode, modifiers)) return true;

        if (keyCode == GLFW.GLFW_KEY_U && (modifiers & GLFW.GLFW_MOD_CONTROL) != 0) {
            UIDebugOverlay.toggle();
            return true;
        }
        if (inputHandler.onKeyPressed(keyCode, scanCode, modifiers)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (ModalManager.onMouseClicked(mouseX, mouseY, button)) return true;
        if (inputHandler.onMouseClicked(mouseX, mouseY, button)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (ModalManager.hasModal()) return true;
        if (inputHandler.onMouseDragged(mouseX, mouseY, button, dx, dy)) return true;
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (ModalManager.hasModal()) return true;
        if (inputHandler.onMouseReleased(mouseX, mouseY, button)) return true;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (ModalManager.hasModal()) return true;
        if (inputHandler.onMouseScrolled(mouseX, mouseY, delta)) return true;
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean charTyped(char c, int modifiers) {
        if (ModalManager.onCharTyped(c, modifiers)) return true;
        if (inputHandler.onCharTyped(c, modifiers)) return true;
        return super.charTyped(c, modifiers);
    }

    @Override
    public void onClose() {
        FocusManager.clearFocus();
        ModalManager.clear();
        super.onClose();
    }

    public void applySync(String key, Object value) {
        viewModel.put(key, value);
        renderer.onViewModelUpdated(key);
    }

    private void sendAction(String actionId) {
        ForgeScreenActionSender.sendAction(spec.getScreenId(), actionId);
    }
}