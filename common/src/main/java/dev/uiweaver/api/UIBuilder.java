package dev.uiweaver.api;

import dev.uiweaver.api.action.ActionContext;
import dev.uiweaver.api.action.ActionDeclaration;
import dev.uiweaver.api.binding.UIBinding;
import dev.uiweaver.api.component.*;
import dev.uiweaver.api.context.UIContextPayload;
import dev.uiweaver.api.slot.SlotBindingDeclaration;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.api.sync.SyncDeclaration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UIBuilder {

    private final String screenId;
    private UIComponent root;
    private UIContextPayload contextPayload = UIContextPayload.NONE;

    private final List<SyncDeclaration<?>>     syncs        = new ArrayList<>();
    private final List<ActionDeclaration>      actions      = new ArrayList<>();
    private final List<UIBinding>              bindings     = new ArrayList<>();
    private final List<SlotBindingDeclaration> slotBindings = new ArrayList<>();

    private UIBuilder(String screenId) { this.screenId = screenId; }

    public static UIBuilder screen(String screenId) { return new UIBuilder(screenId); }

    public ColumnBuilder      column()      { return new ColumnBuilder(); }
    public RowBuilder         row()         { return new RowBuilder(); }
    public GridBuilder     grid(int columns)  { return new GridBuilder(columns); }
    public StackBuilder    stack()            { return new StackBuilder(); }
    public AbsoluteBuilder absolute()         { return new AbsoluteBuilder(); }

    public TabsBuilder tabs() { return new TabsBuilder(); }
    public CheckboxBuilder checkbox(String label)         { return new CheckboxBuilder(label); }
    public CheckboxBuilder checkbox(net.minecraft.network.chat.Component l) { return new CheckboxBuilder(l); }
    public ToggleBuilder   toggle()                       { return new ToggleBuilder(); }
    public SliderBuilder    slider(int min, int max)      { return new SliderBuilder().range(min, max); }
    public DropdownBuilder  dropdown()                     { return new DropdownBuilder(); }

    public ScrollPanelBuilder scrollPanel() { return new ScrollPanelBuilder(); }
    public LabelBuilder  label(String text)    { return new LabelBuilder(Component.literal(text)); }
    public LabelBuilder  label(Component text) { return new LabelBuilder(text); }
    public ButtonBuilder button(String label)  { return new ButtonBuilder(Component.literal(label)); }
    public ButtonBuilder button(Component l)   { return new ButtonBuilder(l); }

    public EnergyBarBuilder       energyBar()                  { return new EnergyBarBuilder(); }
    public FluidBarBuilder        fluidBar()                   { return new FluidBarBuilder(); }
    public ProgressBarBuilder     progressBar()                { return new ProgressBarBuilder(); }
    public SlotGridBuilder        slotGrid(int cols, int rows) { return new SlotGridBuilder(cols, rows); }
    public TextInputBuilder       textInput()                  { return new TextInputBuilder(); }
    public PlayerInventoryBuilder playerInventory()            { return new PlayerInventoryBuilder(); }

    public UIBuilder slots(String name, Supplier<Container> source) {
        slotBindings.add(SlotBindingDeclaration.of(name, source));
        return this;
    }

    public UIBuilder syncInt    (String key, Supplier<Integer> s) { syncs.add(SyncDeclaration.ofInt(key, s));     return this; }
    public UIBuilder syncLong   (String key, Supplier<Long> s)    { syncs.add(SyncDeclaration.ofLong(key, s));    return this; }
    public UIBuilder syncBoolean(String key, Supplier<Boolean> s) { syncs.add(SyncDeclaration.ofBoolean(key, s)); return this; }
    public UIBuilder syncFloat  (String key, Supplier<Float> s)   { syncs.add(SyncDeclaration.ofFloat(key, s));   return this; }
    public UIBuilder syncStringList(String key, Supplier<List<String>> s)      { syncs.add(SyncDeclaration.ofStringList(key, s)); return this; }
    public UIBuilder syncNbtList   (String key, Supplier<List<CompoundTag>> s) { syncs.add(SyncDeclaration.ofNbtList(key, s));    return this; }

    public UIBuilder syncString (String key, Supplier<String> s)  { syncs.add(SyncDeclaration.ofString(key, s));  return this; }

    public UIBuilder action(String actionId, Consumer<ActionContext> handler) {
        actions.add(ActionDeclaration.of(actionId, handler));
        return this;
    }

    public UIBuilder bind(String componentId, String property, String syncKey) {
        bindings.add(UIBinding.of(componentId, property, syncKey));
        return this;
    }

    public UIBuilder context(UIContextPayload payload) { this.contextPayload = payload; return this; }

    public UIBuilder root(UIComponent component)      { this.root = component;        return this; }
    public UIBuilder root(ComponentBuilder<?> builder) { this.root = builder.build(); return this; }

    public UIScreenSpec build() {
        if (root == null) throw new IllegalStateException("Screen '" + screenId + "' has no root component");
        collectAutoBindings(root);
        return UIScreenSpec.builder(screenId)
                .root(root)
                .syncs(syncs)
                .actions(actions)
                .bindings(bindings)
                .slotBindings(slotBindings)
                .context(contextPayload)
                .build();
    }

    private void collectAutoBindings(UIComponent component) {
        if (component instanceof EnergyBarComponent eb && eb.getId() != null && eb.getEnergyKey() != null) {
            bindings.add(UIBinding.of(eb.getId(), eb.getEnergyKey(), eb.getEnergyKey()));
            bindings.add(UIBinding.of(eb.getId(), eb.getMaxEnergyKey(), eb.getMaxEnergyKey()));
        }
        for (UIComponent child : component.getChildren()) {
            collectAutoBindings(child);
        }
    }
}