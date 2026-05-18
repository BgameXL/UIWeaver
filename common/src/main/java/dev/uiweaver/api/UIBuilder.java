package dev.uiweaver.api;

import dev.uiweaver.api.action.ActionContext;
import dev.uiweaver.api.action.ActionDeclaration;
import dev.uiweaver.api.binding.UIBinding;
import dev.uiweaver.api.component.*;
import dev.uiweaver.api.spec.UIContextSpec;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.api.sync.SyncDeclaration;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIBuilder {

    private final String screenId;
    private UIComponent root;
    private UIContextSpec contextSpec = UIContextSpec.NONE;

    private final List<SyncDeclaration<?>> syncs = new ArrayList<>();
    private final List<ActionDeclaration> actions = new ArrayList<>();
    private final List<UIBinding> bindings = new ArrayList<>();

    private UIBuilder(String screenId) {
        this.screenId = screenId;
    }

    public static UIBuilder screen(String screenId) {
        return new UIBuilder(screenId);
    }

    public ColumnBuilder column() { return new ColumnBuilder(); }
    public RowBuilder row() { return new RowBuilder(); }
    public ScrollPanelBuilder scrollPanel() { return new ScrollPanelBuilder(); }

    public LabelBuilder label(String text) { return new LabelBuilder(Component.literal(text)); }
    public LabelBuilder label(Component text) { return new LabelBuilder(text); }
    public ButtonBuilder button(String label) { return new ButtonBuilder(Component.literal(label)); }
    public ButtonBuilder button(Component label) { return new ButtonBuilder(label); }

    public EnergyBarBuilder energyBar() { return new EnergyBarBuilder(); }
    public FluidBarBuilder fluidBar() { return new FluidBarBuilder(); }
    public ProgressBarBuilder progressBar() { return new ProgressBarBuilder(); }
    public SlotGridBuilder slotGrid(int columns, int rows) { return new SlotGridBuilder(columns, rows); }
    public TextInputBuilder textInput() { return new TextInputBuilder(); }

    public UIBuilder syncInt(String key, Supplier<Integer> source) {
        syncs.add(SyncDeclaration.ofInt(key, source));
        return this;
    }

    public UIBuilder syncLong(String key, Supplier<Long> source) {
        syncs.add(SyncDeclaration.ofLong(key, source));
        return this;
    }

    public UIBuilder syncBoolean(String key, Supplier<Boolean> source) {
        syncs.add(SyncDeclaration.ofBoolean(key, source));
        return this;
    }

    public UIBuilder syncFloat(String key, Supplier<Float> source) {
        syncs.add(SyncDeclaration.ofFloat(key, source));
        return this;
    }

    public UIBuilder syncString(String key, Supplier<String> source) {
        syncs.add(SyncDeclaration.ofString(key, source));
        return this;
    }

    public UIBuilder action(String actionId, Consumer<ActionContext> handler) {
        actions.add(ActionDeclaration.of(actionId, handler));
        return this;
    }

    public UIBuilder bind(String componentId, String property, String syncKey) {
        bindings.add(UIBinding.of(componentId, property, syncKey));
        return this;
    }

    public UIBuilder context(UIContextSpec contextSpec) {
        this.contextSpec = contextSpec;
        return this;
    }

    public UIBuilder root(UIComponent component) {
        this.root = component;
        return this;
    }

    public UIBuilder root(ComponentBuilder<?> builder) {
        this.root = builder.build();
        return this;
    }

    public UIScreenSpec build() {
        if (root == null) throw new IllegalStateException("Screen '" + screenId + "' has no root component");
        return UIScreenSpec.builder(screenId)
                .root(root)
                .syncs(syncs)
                .actions(actions)
                .bindings(bindings)
                .context(contextSpec)
                .build();
    }
}