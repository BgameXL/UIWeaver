package dev.uiweaver.runtime.lifecycle;

import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static final ConcurrentHashMap<UUID, UIScreenSession> ACTIVE = new ConcurrentHashMap<>();

    public static UIScreenSession open(ServerPlayer player, UIScreenSession session) {
        ACTIVE.put(player.getUUID(), session);
        return session;
    }

    public static void close(ServerPlayer player) {
        ACTIVE.remove(player.getUUID());
    }

    public static UIScreenSession get(ServerPlayer player) {
        return ACTIVE.get(player.getUUID());
    }

    public static boolean isValid(ServerPlayer player, int sessionId) {
        UIScreenSession session = ACTIVE.get(player.getUUID());
        return session != null && session.getSessionId() == sessionId;
    }
}