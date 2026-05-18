package dev.uiweaver.api.spec;

import dev.uiweaver.api.action.ActionDeclaration;
import dev.uiweaver.api.binding.UIBinding;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.sync.SyncDeclaration;

import java.util.List;
import java.util.Map;

public class UIScreenSpec {

    private final String screenId;
    private final UIComponent root;
    private final List<SyncDeclaration<?>> syncs;
    private final List<ActionDeclaration> actions;
    private final List<UIBinding> bindings;
    private final UIContextSpec contextSpec;

    private UIScreenSpec(Builder builder) {
        this.screenId = builder.screenId;
        this.root = builder.root;
        this.syncs = List.copyOf(builder.syncs);
        this.actions = List.copyOf(builder.actions);
        this.bindings = List.copyOf(builder.bindings);
        this.contextSpec = builder.contextSpec;
    }

    public String getScreenId() { return screenId; }
    public UIComponent getRoot() { return root; }
    public List<SyncDeclaration<?>> getSyncs() { return syncs; }
    public List<ActionDeclaration> getActions() { return actions; }
    public List<UIBinding> getBindings() { return bindings; }
    public UIContextSpec getContextSpec() { return contextSpec; }

    public static Builder builder(String screenId) {
        return new Builder(screenId);
    }

    public static class Builder {
        private final String screenId;
        private UIComponent root;
        private List<SyncDeclaration<?>> syncs = List.of();
        private List<ActionDeclaration> actions = List.of();
        private List<UIBinding> bindings = List.of();
        private UIContextSpec contextSpec = UIContextSpec.NONE;

        private Builder(String screenId) { this.screenId = screenId; }

        public Builder root(UIComponent root) { this.root = root; return this; }
        public Builder syncs(List<SyncDeclaration<?>> syncs) { this.syncs = syncs; return this; }
        public Builder actions(List<ActionDeclaration> actions) { this.actions = actions; return this; }
        public Builder bindings(List<UIBinding> bindings) { this.bindings = bindings; return this; }
        public Builder context(UIContextSpec contextSpec) { this.contextSpec = contextSpec; return this; }

        public UIScreenSpec build() {
            if (root == null) throw new IllegalStateException("UIScreenSpec requires a root component");
            return new UIScreenSpec(this);
        }
    }
}
