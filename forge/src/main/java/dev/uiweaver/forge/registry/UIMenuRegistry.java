package dev.uiweaver.forge.registry;

import dev.uiweaver.api.UIBuilder;
import dev.uiweaver.api.UIScreen;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.forge.client.screen.UIContainerScreen;
import dev.uiweaver.forge.network.ForgeNetworkChannel;
import dev.uiweaver.runtime.menu.UIMenu;
import dev.uiweaver.runtime.registry.UIRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class UIMenuRegistry {

    private static RegistryObject<MenuType<UIMenu>> GENERIC_MENU;
    private static ForgeNetworkChannel networkChannel;

    public static void setChannel(ForgeNetworkChannel channel) {
        networkChannel = channel;
    }

    public static void register(DeferredRegister<MenuType<?>> register) {
        GENERIC_MENU = register.register("generic_menu", () ->
                IForgeMenuType.create((containerId, inventory, buf) -> {
                    String screenId = buf.readUtf();
                    boolean hasPos = buf.readBoolean();
                    BlockPos pos = hasPos ? buf.readBlockPos() : null;

                    UIScreen factory = UIRegistry.getFactory(screenId);
                    if (factory == null) return null;

                    UIScreenSpec spec = factory.build(UIBuilder.screen(screenId));
                    UIMenu menu = new UIMenu(GENERIC_MENU.get(), containerId, inventory, spec, networkChannel);
                    menu.setOpenPos(pos);
                    return menu;
                })
        );
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreen() {
        MenuScreens.<UIMenu, UIContainerScreen>register(
                GENERIC_MENU.get(),
                (menu, inventory, title) -> new UIContainerScreen(
                        menu, inventory, Component.literal(menu.getSpec().getScreenId()))
        );
    }

    public static void open(ServerPlayer player, UIScreen screen, String screenId, BlockPos pos) {
        UIRegistry.registerFactory(screenId, screen);
        UIScreenSpec spec = screen.build(UIBuilder.screen(screenId));

        NetworkHooks.openScreen(player, new MenuProvider() {
            @Override public Component getDisplayName() { return Component.literal(screenId); }

            @Override
            public UIMenu createMenu(int containerId, Inventory inventory, Player p) {
                UIMenu menu = new UIMenu(GENERIC_MENU.get(), containerId, inventory, spec, networkChannel);
                menu.setOpenPos(pos);
                return menu;
            }
        }, buf -> {
            buf.writeUtf(screenId);
            buf.writeBoolean(pos != null);
            if (pos != null) buf.writeBlockPos(pos);
        });

        if (player.containerMenu instanceof UIMenu menu) {
            menu.broadcastChanges();
        }
    }

    public static void open(ServerPlayer player, String screenId, BlockPos pos) {
        UIScreen factory = UIRegistry.getFactory(screenId);
        if (factory == null) throw new IllegalArgumentException("No factory for screen: " + screenId);
        open(player, factory, screenId, pos);
    }

    public static void open(ServerPlayer player, String screenId) {
        open(player, screenId, null);
    }
}