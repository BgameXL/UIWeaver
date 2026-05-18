package dev.uiweaver.runtime.action;

import dev.uiweaver.api.action.ActionDeclaration;
import dev.uiweaver.api.spec.UIContextSpec;
import dev.uiweaver.runtime.context.ContextValidator;
import dev.uiweaver.runtime.context.RuntimeActionContext;
import dev.uiweaver.runtime.menu.UIMenu;
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
    private final UIContextSpec contextSpec;

    public ActionDispatcher(String screenId, List<ActionDeclaration> actions, UIContextSpec contextSpec) {
        this.screenId = screenId;
        this.actions = actions.stream()
                .collect(Collectors.toMap(ActionDeclaration::getActionId, Function.identity()));
        this.contextSpec = contextSpec;
    }

    public void dispatch(ActionPacket packet, ServerPlayer player) {
        // Validate the packet targets the screen this player actually has open
        if (!(player.containerMenu instanceof UIMenu openMenu)
                || !openMenu.getSpec().getScreenId().equals(packet.getScreenId())) {
            LOGGER.warn("Player {} sent action for screen '{}' but has '{}' open",
                    player.getName().getString(), packet.getScreenId(),
                    player.containerMenu == null ? "none" : player.containerMenu.getClass().getSimpleName());
            return;
        }

        ActionDeclaration decl = actions.get(packet.getActionId());
        if (decl == null) {
            LOGGER.warn("Unknown action '{}' in screen '{}' from player {}",
                    packet.getActionId(), screenId, player.getName().getString());
            return;
        }

        ContextValidator.Result result = ContextValidator.validate(player, packet.getPos(), contextSpec);
        if (result != ContextValidator.Result.OK) {
            LOGGER.debug("Action '{}' rejected for player {}: {}",
                    packet.getActionId(), player.getName().getString(), result);
            return;
        }

        try {
            decl.getHandler().accept(new RuntimeActionContext(player, packet.getPos(), packet.getActionId()));
        } catch (Exception e) {
            LOGGER.error("Error executing action '{}' for player {}",
                    packet.getActionId(), player.getName().getString(), e);
        }
    }
}