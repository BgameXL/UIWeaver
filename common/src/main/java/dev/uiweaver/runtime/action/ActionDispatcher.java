package dev.uiweaver.runtime.action;

import dev.uiweaver.api.action.ActionDeclaration;
import dev.uiweaver.api.context.UIContextPayload;
import dev.uiweaver.runtime.context.ContextValidator;
import dev.uiweaver.runtime.context.RuntimeActionContext;
import dev.uiweaver.runtime.lifecycle.SessionManager;
import dev.uiweaver.runtime.network.ActionPacket;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ActionDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionDispatcher.class);

    private final String screenId;
    private final Map<String, ActionDeclaration> actions;
    private final UIContextPayload contextPayload;

    public ActionDispatcher(String screenId, List<ActionDeclaration> actions, UIContextPayload contextPayload) {
        this.screenId       = screenId;
        this.actions        = actions.stream().collect(Collectors.toMap(ActionDeclaration::getActionId, Function.identity()));
        this.contextPayload = contextPayload;
    }

    public void dispatch(ActionPacket packet, ServerPlayer player) {
        if (!SessionManager.isValid(player, packet.getSessionId())) {
            LOGGER.warn("[UIWeaver] Invalid session {} for player {}", packet.getSessionId(), player.getName().getString());
            return;
        }

        if (!screenId.equals(packet.getScreenId())) {
            LOGGER.warn("[UIWeaver] Screen mismatch: expected '{}', got '{}'", screenId, packet.getScreenId());
            return;
        }

        ActionDeclaration decl = actions.get(packet.getActionId());
        if (decl == null) {
            LOGGER.warn("[UIWeaver] Unknown action '{}' in screen '{}'", packet.getActionId(), screenId);
            return;
        }

        ContextValidator.Result result = ContextValidator.validate(player, contextPayload);
        if (result != ContextValidator.Result.OK) {
            LOGGER.debug("[UIWeaver] Action '{}' rejected for {}: {}", packet.getActionId(), player.getName().getString(), result);
            return;
        }

        decl.getHandler().accept(new RuntimeActionContext(player, contextPayload, packet.getActionId(), packet.getPayload()));
    }
}