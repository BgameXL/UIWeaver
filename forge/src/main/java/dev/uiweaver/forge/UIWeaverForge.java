package dev.uiweaver.forge;

import dev.uiweaver.forge.client.ForgeClientSetup;
import dev.uiweaver.forge.network.ForgeNetworkChannel;
import dev.uiweaver.forge.registry.UIMenuRegistry;
import dev.uiweaver.runtime.menu.UIMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod("uiweaver")
public class UIWeaverForge {

    private static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, "uiweaver");

    private static final ForgeNetworkChannel NETWORK = new ForgeNetworkChannel();

    public UIWeaverForge() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        UIMenuRegistry.setChannel(NETWORK);
        UIMenuRegistry.register(MENUS);
        MENUS.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(NETWORK::init);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ForgeClientSetup.init(NETWORK));
    }
}