package dev.uiweaver.forge.client;

import dev.uiweaver.client.screen.ClientNetworkBridge;
import dev.uiweaver.forge.client.render.WidgetRendererSetup;
import dev.uiweaver.forge.network.ForgeNetworkChannel;
import dev.uiweaver.forge.registry.UIMenuRegistry;

public class ForgeClientSetup {

    public static void init(ForgeNetworkChannel channel) {
        ClientNetworkBridge.register(channel::sendToServer);
        WidgetRendererSetup.init();
        UIMenuRegistry.registerScreen();
    }
}