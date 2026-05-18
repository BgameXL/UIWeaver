package dev.uiweaver.fabric.network;

import dev.uiweaver.fabric.client.screen.UIContainerScreen;
import dev.uiweaver.runtime.network.SyncPacket;
import net.minecraft.client.Minecraft;

public class ClientSyncHandler {

    public static void handle(SyncPacket packet) {
        var mc = Minecraft.getInstance();
        if (!(mc.screen instanceof UIContainerScreen screen)) return;
        if (!screen.getMenu().getSpec().getScreenId().equals(packet.getScreenId())) return;

        for (var entry : packet.getEntries()) {
            screen.applySync(entry.key(), entry.value());
        }
    }
}