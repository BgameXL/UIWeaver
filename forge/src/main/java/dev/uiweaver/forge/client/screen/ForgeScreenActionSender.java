package dev.uiweaver.forge.client.screen;

import dev.uiweaver.client.screen.ClientNetworkBridge;
import dev.uiweaver.runtime.menu.UIMenu;
import dev.uiweaver.runtime.network.ActionPacket;

public class ForgeScreenActionSender {

    public static void send(String screenId, String actionId, UIMenu menu) {
        ActionPacket packet = new ActionPacket(screenId, actionId, menu.getOpenPos());
        ClientNetworkBridge.sendToServer(packet);
    }
}