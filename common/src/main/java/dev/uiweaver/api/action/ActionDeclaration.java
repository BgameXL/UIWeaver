package dev.uiweaver.api.action;

import java.util.function.Consumer;

public class ActionDeclaration {

    private final String actionId;
    private final Consumer<ActionContext> handler;
    private final int debounceMs;

    private ActionDeclaration(String actionId, Consumer<ActionContext> handler, int debounceMs) {
        this.actionId = actionId;
        this.handler = handler;
        this.debounceMs = debounceMs;
    }

    public static ActionDeclaration of(String actionId, Consumer<ActionContext> handler) {
        return new ActionDeclaration(actionId, handler, 0);
    }

    public ActionDeclaration debounce(int ms) {
        return new ActionDeclaration(actionId, handler, ms);
    }

    public String getActionId() { return actionId; }
    public Consumer<ActionContext> getHandler() { return handler; }
    public int getDebounceMs() { return debounceMs; }
}
