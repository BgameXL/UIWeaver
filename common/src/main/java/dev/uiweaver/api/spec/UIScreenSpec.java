package dev.uiweaver.api.spec;

import dev.uiweaver.api.action.ActionDeclaration;
import dev.uiweaver.api.binding.UIBinding;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.context.UIContextPayload;
import dev.uiweaver.api.slot.SlotBindingDeclaration;
import dev.uiweaver.api.sync.SyncDeclaration;

import java.util.List;

public class UIScreenSpec {

    private final String screenId;
    private final UIComponent root;
    private final List<SyncDeclaration<?>> syncs;
    private final List<ActionDeclaration> actions;
    private final List<UIBinding> bindings;
    private final List<SlotBindingDeclaration> slotBindings;
    private final UIContextPayload contextPayload;

    private UIScreenSpec(Builder builder) {
        this.screenId       = builder.screenId;
        this.root           = builder.root;
        this.syncs          = List.copyOf(builder.syncs);
        this.actions        = List.copyOf(builder.actions);
        this.bindings       = List.copyOf(builder.bindings);
        this.slotBindings   = List.copyOf(builder.slotBindings);
        this.contextPayload = builder.contextPayload;
    }

    public String getScreenId()                           { return screenId; }
    public UIComponent getRoot()                          { return root; }
    public List<SyncDeclaration<?>> getSyncs()            { return syncs; }
    public List<ActionDeclaration> getActions()           { return actions; }
    public List<UIBinding> getBindings()                  { return bindings; }
    public List<SlotBindingDeclaration> getSlotBindings() { return slotBindings; }
    public UIContextPayload getContextPayload()           { return contextPayload; }

    public static Builder builder(String screenId) { return new Builder(screenId); }

    public static class Builder {
        private final String screenId;
        private UIComponent root;
        private List<SyncDeclaration<?>>     syncs        = List.of();
        private List<ActionDeclaration>      actions      = List.of();
        private List<UIBinding>              bindings     = List.of();
        private List<SlotBindingDeclaration> slotBindings = List.of();
        private UIContextPayload             contextPayload = UIContextPayload.NONE;

        private Builder(String screenId) { this.screenId = screenId; }

        public Builder root(UIComponent root)                        { this.root = root;               return this; }
        public Builder syncs(List<SyncDeclaration<?>> syncs)         { this.syncs = syncs;             return this; }
        public Builder actions(List<ActionDeclaration> actions)       { this.actions = actions;         return this; }
        public Builder bindings(List<UIBinding> bindings)             { this.bindings = bindings;       return this; }
        public Builder slotBindings(List<SlotBindingDeclaration> sb)  { this.slotBindings = sb;         return this; }
        public Builder context(UIContextPayload payload)              { this.contextPayload = payload;  return this; }

        public UIScreenSpec build() {
            if (root == null) throw new IllegalStateException("UIScreenSpec requires a root component");
            UIScreenSpec spec = new UIScreenSpec(this);
            SpecValidator.validate(spec);
            return spec;
        }
    }
}