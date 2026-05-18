package dev.uiweaver.client.screen;

import dev.uiweaver.runtime.network.ActionPacket;

public class ClientNetworkBridge {

    private static ScreenActionSender sender;

    public static void register(ScreenActionSender s) {
        sender = s;
    }

    public static void sendToServer(ActionPacket packet) {
        if (sender == null) throw new IllegalStateException("ClientNetworkBridge has no sender registered");
        sender.send(packet);
    }
}