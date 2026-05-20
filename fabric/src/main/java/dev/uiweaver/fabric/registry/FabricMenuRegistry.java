package dev.uiweaver.fabric.registry;

import dev.uiweaver.api.UIBuilder;
import dev.uiweaver.api.UIScreen;
import dev.uiweaver.api.context.UIContextPayload;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.fabric.client.screen.UIContainerScreen;
import dev.uiweaver.fabric.network.FabricNetworkChannel;
import dev.uiweaver.runtime.lifecycle.SessionManager;
import dev.uiweaver.runtime.lifecycle.UIScreenSession;
import dev.uiweaver.runtime.menu.UIMenu;
import dev.uiweaver.runtime.registry.UIRegistry;
import dev.uiweaver.fabric.client.screen.UIContainerScreen;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

public class FabricMenuRegistry {

    public static MenuType<UIMenu> GENERIC_MENU;
    private static FabricNetworkChannel channel;

    public static void setChannel(FabricNetworkChannel ch) { channel = ch; }

    public static void register() {
        GENERIC_MENU = Registry.register(
                BuiltInRegistries.MENU,
                new ResourceLocation("uiweaver", "generic_menu"),
                new ExtendedScreenHandlerType<>((containerId, inventory, buf) -> {
                    String screenId          = buf.readUtf();
                    int sessionId            = buf.readInt();
                    UIContextPayload payload = UIContextPayload.decode(buf);

                    UIScreen factory = UIRegistry.getFactory(screenId);
                    if (factory == null) return null;

                    UIScreenSpec spec = factory.build(UIBuilder.screen(screenId));
                    UIMenu menu = new UIMenu(GENERIC_MENU, containerId, inventory, spec, channel);
                    menu.setClientSessionId(sessionId);
                    return menu;
                })
        );
    }

    public static void registerScreen() {
        net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry.register(
                GENERIC_MENU,
                (net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry.Factory<UIMenu, UIContainerScreen>)
                        (menu, inventory, title) -> new UIContainerScreen(menu, inventory, Component.literal(menu.getSpec().getScreenId()))
        );
    }

    public static void open(ServerPlayer player, UIScreen screen, String screenId, UIContextPayload payload) {
        UIRegistry.registerFactory(screenId, screen);
        UIScreenSpec spec = screen.build(UIBuilder.screen(screenId));
        UIScreenSession session = new UIScreenSession(player, spec);
        SessionManager.open(player, session);

        player.openMenu(new ExtendedScreenHandlerFactory() {
            @Override public Component getDisplayName() { return Component.literal(screenId); }

            @Override
            public UIMenu createMenu(int containerId, Inventory inventory, Player p) {
                UIMenu menu = new UIMenu(GENERIC_MENU, containerId, inventory, spec, channel);
                menu.onOpened(player);
                return menu;
            }

            @Override
            public void writeScreenOpeningData(ServerPlayer p, FriendlyByteBuf buf) {
                buf.writeUtf(screenId);
                buf.writeInt(session.getSessionId());
                UIContextPayload.encode(payload, buf);
            }
        });
    }

    public static void open(ServerPlayer player, String screenId, UIContextPayload payload) {
        UIScreen factory = UIRegistry.getFactory(screenId);
        if (factory == null) throw new IllegalArgumentException("No factory for screen: " + screenId);
        open(player, factory, screenId, payload);
    }

    public static void open(ServerPlayer player, String screenId) {
        open(player, screenId, UIContextPayload.NONE);
    }
}