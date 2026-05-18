package dev.uiweaver.fabric;

import dev.uiweaver.fabric.network.FabricNetworkChannel;
import dev.uiweaver.fabric.registry.FabricMenuRegistry;
import net.fabricmc.api.ModInitializer;

public class UIWeaverFabric implements ModInitializer {

    public static final FabricNetworkChannel NETWORK = new FabricNetworkChannel();

    @Override
    public void onInitialize() {
        FabricMenuRegistry.setChannel(NETWORK);
        FabricMenuRegistry.register();
        NETWORK.initServer();
    }
}