package dev.uiweaver.forge.client.screen;

import dev.uiweaver.client.screen.ClientNetworkBridge;
import dev.uiweaver.client.screen.ScreenActionSender;
import dev.uiweaver.forge.network.ForgeNetworkChannel;
import dev.uiweaver.runtime.menu.UIMenu;
import dev.uiweaver.runtime.network.ActionPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;

public class ForgeScreenActionSender implements ScreenActionSender {

    private final ForgeNetworkChannel channel;

    public ForgeScreenActionSender(ForgeNetworkChannel channel) {
        this.channel = channel;
    }

    @Override
    public void send(ActionPacket packet) {
        channel.sendToServer(packet);
    }

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