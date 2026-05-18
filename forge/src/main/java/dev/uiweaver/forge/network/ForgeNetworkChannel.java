package dev.uiweaver.forge.network;

import dev.uiweaver.runtime.network.ActionPacket;
import dev.uiweaver.runtime.network.NetworkChannel;
import dev.uiweaver.runtime.network.SyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class ForgeNetworkChannel implements NetworkChannel {

    private static final String VERSION = "1";
    private SimpleChannel channel;

    public void init() {
        channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("uiweaver", "main"),
                () -> VERSION,
                VERSION::equals,
                VERSION::equals
        );

        channel.registerMessage(0, SyncPacket.class,
                SyncPacket::encode,
                SyncPacket::decode,
                (packet, ctx) -> {
                    ctx.get().enqueueWork(() -> ClientSyncHandler.handle(packet));
                    ctx.get().setPacketHandled(true);
                },
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );

        channel.registerMessage(1, ActionPacket.class,
                ActionPacket::encode,
                ActionPacket::decode,
                (packet, ctx) -> {
                    ServerPlayer player = ctx.get().getSender();
                    if (player != null) {
                        ctx.get().enqueueWork(() -> ServerActionHandler.handle(packet, player));
                    }
                    ctx.get().setPacketHandled(true);
                },
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
    }

    @Override
    public void sendToClient(ServerPlayer player, SyncPacket packet) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public void sendToServer(ActionPacket packet) {
        channel.sendToServer(packet);
    }

    @Override
    public void registerServerHandler(String packetId, ServerPacketHandler handler) {
        // handlers registered statically in init()
    }
}