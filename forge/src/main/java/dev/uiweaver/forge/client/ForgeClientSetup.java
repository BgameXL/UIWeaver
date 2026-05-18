package dev.uiweaver.forge.client;

import dev.uiweaver.api.component.MeasureProvider;
import dev.uiweaver.client.screen.ClientNetworkBridge;
import dev.uiweaver.forge.client.render.WidgetRendererSetup;
import dev.uiweaver.forge.client.screen.ForgeScreenActionSender;
import dev.uiweaver.forge.network.ForgeNetworkChannel;
import dev.uiweaver.forge.registry.UIMenuRegistry;
import net.minecraft.client.Minecraft;

public class ForgeClientSetup {

    public static void init(ForgeNetworkChannel channel) {
        MeasureProvider.register(text -> Minecraft.getInstance().font.width(text));
        ClientNetworkBridge.register(new ForgeScreenActionSender(channel));
        WidgetRendererSetup.init();
        UIMenuRegistry.registerScreen();
    }
}