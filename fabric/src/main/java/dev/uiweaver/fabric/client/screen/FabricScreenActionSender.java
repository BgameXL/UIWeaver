package dev.uiweaver.fabric.client.screen;

import dev.uiweaver.client.screen.ClientNetworkBridge;
import dev.uiweaver.runtime.menu.UIMenu;
import dev.uiweaver.runtime.network.ActionPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;

public class FabricScreenActionSender {

    public static void sendAction(String screenId, String actionId) {
        sendAction(screenId, actionId, new CompoundTag());
    }

    public static void sendAction(String screenId, String actionId, CompoundTag payload) {
        int sessionId = -1;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.containerMenu instanceof UIMenu menu) {
            sessionId = menu.getClientSessionId();
        }
        ClientNetworkBridge.sendToServer(new ActionPacket(sessionId, screenId, actionId, payload));
    }
}
