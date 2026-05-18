package dev.uiweaver.fabric.network;

import dev.uiweaver.runtime.network.ActionPacket;
import dev.uiweaver.runtime.network.NetworkChannel;
import dev.uiweaver.runtime.network.SyncPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworkChannel implements NetworkChannel {

    private static final ResourceLocation SYNC_ID = new ResourceLocation("uiweaver", "sync");
    private static final ResourceLocation ACTION_ID = new ResourceLocation("uiweaver", "action");

    public void initServer() {
        ServerPlayNetworking.registerGlobalReceiver(ACTION_ID, (server, player, handler, buf, responseSender) -> {
            ActionPacket packet = ActionPacket.decode(buf);
            server.execute(() -> ServerActionHandler.handle(packet, player));
        });
    }

    public void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(SYNC_ID, (client, handler, buf, responseSender) -> {
            SyncPacket packet = SyncPacket.decode(buf);
            client.execute(() -> ClientSyncHandler.handle(packet));
        });
    }

    @Override
    public void sendToClient(ServerPlayer player, SyncPacket packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.encode(buf);
        ServerPlayNetworking.send(player, SYNC_ID, buf);
    }

    public void sendToServer(ActionPacket packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.encode(buf);
        ClientPlayNetworking.send(ACTION_ID, buf);
    }

    @Override
    public void registerServerHandler(String packetId, ServerPacketHandler handler) {
        // handlers registered globally in initServer()
    }
}