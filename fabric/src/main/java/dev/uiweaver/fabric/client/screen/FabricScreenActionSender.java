package dev.uiweaver.fabric.client.screen;

import dev.uiweaver.client.screen.ClientNetworkBridge;
import dev.uiweaver.runtime.menu.UIMenu;
import dev.uiweaver.runtime.network.ActionPacket;

public class FabricScreenActionSender {

    public static void send(String screenId, String actionId, UIMenu menu) {
        ActionPacket packet = new ActionPacket(screenId, actionId, menu.getOpenPos());
        ClientNetworkBridge.sendToServer(packet);
    }
}