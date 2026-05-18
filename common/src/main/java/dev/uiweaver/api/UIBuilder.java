package dev.uiweaver.api;

import dev.uiweaver.api.action.ActionContext;
import dev.uiweaver.api.action.ActionDeclaration;
import dev.uiweaver.api.binding.UIBinding;
import dev.uiweaver.api.component.*;
import dev.uiweaver.api.slot.SlotBindingDeclaration;
import dev.uiweaver.api.spec.UIContextSpec;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.api.sync.SyncDeclaration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIBuilder {

    private final String screenId;
    private UIComponent root;
    private UIContextSpec contextSpec = UIContextSpec.NONE;

    private final List<SyncDeclaration<?>>     syncs        = new ArrayList<>();
    private final List<ActionDeclaration>      actions      = new ArrayList<>();
    private final List<UIBinding>              bindings     = new ArrayList<>();
    private final List<SlotBindingDeclaration> slotBindings = new ArrayList<>();

    private UIBuilder(String screenId) { this.screenId = screenId; }

    public static UIBuilder screen(String screenId) { return new UIBuilder(screenId); }

    // ── Layout ─────────────────────────────────────────────────────────────

    public ColumnBuilder      column()              { return new ColumnBuilder(); }
    public RowBuilder         row()                 { return new RowBuilder(); }
    public ScrollPanelBuilder scrollPanel()         { return new ScrollPanelBuilder(); }

    // ── Basic widgets ──────────────────────────────────────────────────────

    public LabelBuilder  label(String text)    { return new LabelBuilder(Component.literal(text)); }
    public LabelBuilder  label(Component text) { return new LabelBuilder(text); }
    public ButtonBuilder button(String label)  { return new ButtonBuilder(Component.literal(label)); }
    public ButtonBuilder button(Component l)   { return new ButtonBuilder(l); }

    // ── Tech widgets ───────────────────────────────────────────────────────

    public EnergyBarBuilder      energyBar()                  { return new EnergyBarBuilder(); }
    public FluidBarBuilder       fluidBar()                   { return new FluidBarBuilder(); }
    public ProgressBarBuilder    progressBar()                { return new ProgressBarBuilder(); }
    public SlotGridBuilder       slotGrid(int cols, int rows) { return new SlotGridBuilder(cols, rows); }
    public TextInputBuilder      textInput()                  { return new TextInputBuilder(); }
    public PlayerInventoryBuilder playerInventory()           { return new PlayerInventoryBuilder(); }

    // ── Slot bindings ──────────────────────────────────────────────────────

    public UIBuilder slots(String name, Supplier<Container> source) {
        slotBindings.add(SlotBindingDeclaration.of(name, source));
        return this;
    }

    // ── Sync ───────────────────────────────────────────────────────────────

    public UIBuilder syncInt    (String key, Supplier<Integer> s) { syncs.add(SyncDeclaration.ofInt(key, s));     return this; }
    public UIBuilder syncLong   (String key, Supplier<Long> s)    { syncs.add(SyncDeclaration.ofLong(key, s));    return this; }
    public UIBuilder syncBoolean(String key, Supplier<Boolean> s) { syncs.add(SyncDeclaration.ofBoolean(key, s)); return this; }
    public UIBuilder syncFloat  (String key, Supplier<Float> s)   { syncs.add(SyncDeclaration.ofFloat(key, s));   return this; }
    public UIBuilder syncString (String key, Supplier<String> s)  { syncs.add(SyncDeclaration.ofString(key, s));  return this; }

    // ── Actions ────────────────────────────────────────────────────────────

    public UIBuilder action(String actionId, Consumer<ActionContext> handler) {
        actions.add(ActionDeclaration.of(actionId, handler));
        return this;
    }

    // ── Explicit bindings ──────────────────────────────────────────────────

    public UIBuilder bind(String componentId, String property, String syncKey) {
        bindings.add(UIBinding.of(componentId, property, syncKey));
        return this;
    }

    // ── Context ────────────────────────────────────────────────────────────

    public UIBuilder context(UIContextSpec ctx) { this.contextSpec = ctx; return this; }

    // ── Root ───────────────────────────────────────────────────────────────

    public UIBuilder root(UIComponent component) { this.root = component; return this; }

    public UIBuilder root(ComponentBuilder<?> builder) {
        this.root = builder.build();
        return this;
    }

    // ── Build ──────────────────────────────────────────────────────────────

    public UIScreenSpec build() {
        if (root == null) throw new IllegalStateException("Screen '" + screenId + "' has no root component");
        collectAutoBindings(root);
        return UIScreenSpec.builder(screenId)
                .root(root)
                .syncs(syncs)
                .actions(actions)
                .bindings(bindings)
                .slotBindings(slotBindings)
                .context(contextSpec)
                .build();
    }

    private void collectAutoBindings(UIComponent component) {
        if (component instanceof EnergyBarComponent eb && eb.getId() != null && eb.getEnergyKey() != null) {
            bindings.add(UIBinding.of(eb.getId(), eb.getEnergyKey(),    eb.getEnergyKey()));
            bindings.add(UIBinding.of(eb.getId(), eb.getMaxEnergyKey(), eb.getMaxEnergyKey()));
        }
        for (UIComponent child : component.getChildren()) {
            collectAutoBindings(child);
        }
    }
}