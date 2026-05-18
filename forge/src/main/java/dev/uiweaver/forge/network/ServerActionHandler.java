package dev.uiweaver.forge.network;

import dev.uiweaver.runtime.menu.UIMenu;
import dev.uiweaver.runtime.network.ActionPacket;
import net.minecraft.server.level.ServerPlayer;

public class ServerActionHandler {

    public static void handle(ActionPacket packet, ServerPlayer player) {
        if (player.containerMenu instanceof UIMenu menu) {
            menu.handleAction(packet, player);
        }
    }
}