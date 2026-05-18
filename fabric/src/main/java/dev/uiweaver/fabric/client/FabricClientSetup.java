package dev.uiweaver.fabric.client;

import dev.uiweaver.client.screen.ClientNetworkBridge;
import dev.uiweaver.fabric.client.render.WidgetRendererSetup;
import dev.uiweaver.fabric.client.screen.UIContainerScreen;
import dev.uiweaver.fabric.network.FabricNetworkChannel;
import dev.uiweaver.fabric.registry.FabricMenuRegistry;
import dev.uiweaver.runtime.menu.UIMenu;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.network.chat.Component;

public class FabricClientSetup {

    public static void init(FabricNetworkChannel channel) {
        channel.initClient();
        ClientNetworkBridge.register(channel::sendToServer);
        WidgetRendererSetup.init();

        ScreenRegistry.<UIMenu, UIContainerScreen>register(
                FabricMenuRegistry.GENERIC_MENU,
                (menu, inventory, title) -> new UIContainerScreen(
                        menu, inventory, Component.literal(menu.getSpec().getScreenId()))
        );
    }
}