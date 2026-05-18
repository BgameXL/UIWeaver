package dev.uiweaver.fabric.client.screen;

import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.api.view.UIViewModel;
import dev.uiweaver.fabric.client.input.InputHandler;
import dev.uiweaver.fabric.client.render.UIRenderer;
import dev.uiweaver.runtime.menu.UIMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class UIContainerScreen extends AbstractContainerScreen<UIMenu> {

    private final UIScreenSpec spec;
    private final UIViewModel viewModel;
    private UIRenderer renderer;
    private InputHandler inputHandler;

    public UIContainerScreen(UIMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.spec = menu.getSpec();
        this.viewModel = new UIViewModel();
    }

    @Override
    protected void init() {
        super.init();
        this.renderer = new UIRenderer(spec, viewModel);
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
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (inputHandler.onMouseClicked(mouseX, mouseY, button)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (inputHandler.onMouseScrolled(mouseX, mouseY, delta)) return true;
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (inputHandler.onKeyPressed(keyCode, scanCode, modifiers)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char c, int modifiers) {
        if (inputHandler.onCharTyped(c, modifiers)) return true;
        return super.charTyped(c, modifiers);
    }

    public void applySync(String key, Object value) {
        viewModel.put(key, value);
        renderer.onViewModelUpdated(key);
    }

    private void sendAction(String actionId) {
        FabricScreenActionSender.send(spec.getScreenId(), actionId, menu);
    }
}