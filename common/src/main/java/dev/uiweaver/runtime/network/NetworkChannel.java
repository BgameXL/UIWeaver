package dev.uiweaver.runtime.network;

import net.minecraft.server.level.ServerPlayer;

public interface NetworkChannel {

    void sendToClient(ServerPlayer player, SyncPacket packet);

    void registerServerHandler(String packetId, ServerPacketHandler handler);

    @FunctionalInterface
    interface ServerPacketHandler {
        void handle(ActionPacket packet, ServerPlayer sender);
    }
}