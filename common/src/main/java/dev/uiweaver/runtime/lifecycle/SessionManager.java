package dev.uiweaver.runtime.lifecycle;

import dev.uiweaver.runtime.menu.UIMenu;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static final ConcurrentHashMap<UUID, UIMenu> ACTIVE = new ConcurrentHashMap<>();

    public static void onOpen(ServerPlayer player, UIMenu menu) {
        ACTIVE.put(player.getUUID(), menu);
        menu.forceFullSync();
    }

    public static void onClose(ServerPlayer player) {
        ACTIVE.remove(player.getUUID());
    }

    public static UIMenu getMenu(ServerPlayer player) {
        return ACTIVE.get(player.getUUID());
    }

    public static boolean hasOpen(ServerPlayer player) {
        return ACTIVE.containsKey(player.getUUID());
    }
}