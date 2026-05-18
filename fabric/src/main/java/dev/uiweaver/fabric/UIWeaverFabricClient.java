package dev.uiweaver.fabric;

import dev.uiweaver.fabric.client.FabricClientSetup;
import net.fabricmc.api.ClientModInitializer;

public class UIWeaverFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricClientSetup.init(UIWeaverFabric.NETWORK);
    }
}