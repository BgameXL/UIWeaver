package dev.uiweaver.runtime.lifecycle;

import dev.uiweaver.api.context.UIContextPayload;
import dev.uiweaver.api.spec.UIScreenSpec;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.atomic.AtomicInteger;

public class UIScreenSession {

    private static final AtomicInteger ID_GEN = new AtomicInteger(1);

    private final int sessionId;
    private final String screenId;
    private final ServerPlayer player;
    private final UIContextPayload context;
    private final UIScreenSpec spec;

    public UIScreenSession(ServerPlayer player, UIScreenSpec spec) {
        this.sessionId = ID_GEN.getAndIncrement();
        this.screenId  = spec.getScreenId();
        this.player    = player;
        this.context   = spec.getContextPayload();
        this.spec      = spec;
    }

    public int getSessionId()          { return sessionId; }
    public String getScreenId()        { return screenId; }
    public ServerPlayer getPlayer()    { return player; }
    public UIContextPayload getContext() { return context; }
    public UIScreenSpec getSpec()      { return spec; }
}