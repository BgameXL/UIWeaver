package dev.uiweaver.fabric.registry;

import dev.uiweaver.api.UIBuilder;
import dev.uiweaver.api.UIScreen;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.fabric.network.FabricNetworkChannel;
import dev.uiweaver.runtime.menu.UIMenu;
import dev.uiweaver.runtime.registry.UIRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
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
                    String screenId = buf.readUtf();
                    boolean hasPos = buf.readBoolean();
                    BlockPos pos = hasPos ? buf.readBlockPos() : null;

                    UIScreen factory = UIRegistry.getFactory(screenId);
                    if (factory == null) return null;

                    UIScreenSpec spec = factory.build(UIBuilder.screen(screenId));
                    UIMenu menu = new UIMenu(GENERIC_MENU, containerId, inventory, spec, channel);
                    menu.setOpenPos(pos);
                    return menu;
                })
        );
    }

    public static void open(ServerPlayer player, UIScreen screen, String screenId, BlockPos pos) {
        UIRegistry.registerFactory(screenId, screen);
        UIScreenSpec spec = screen.build(UIBuilder.screen(screenId));

        player.openMenu(new ExtendedScreenHandlerFactory() {
            @Override public Component getDisplayName() { return Component.literal(screenId); }

            @Override
            public UIMenu createMenu(int containerId, Inventory inventory, Player p) {
                UIMenu menu = new UIMenu(GENERIC_MENU, containerId, inventory, spec, channel);
                menu.setOpenPos(pos);
                return menu;
            }

            @Override
            public void writeScreenOpeningData(ServerPlayer p, FriendlyByteBuf buf) {
                buf.writeUtf(screenId);
                buf.writeBoolean(pos != null);
                if (pos != null) buf.writeBlockPos(pos);
            }
        });
    }

    public static void open(ServerPlayer player, String screenId, BlockPos pos) {
        UIScreen factory = UIRegistry.getFactory(screenId);
        if (factory == null) throw new IllegalArgumentException("No factory for: " + screenId);
        open(player, factory, screenId, pos);
    }

    public static void open(ServerPlayer player, String screenId) {
        open(player, screenId, null);
    }
}