package dev.uiweaver.api.component;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class DropdownBuilder extends ComponentBuilder<DropdownBuilder> {

    private final List<Component> options = new ArrayList<>();
    private int selectedIndex = 0;
    private String actionId   = null;

    public DropdownBuilder option(String label)      { options.add(Component.literal(label)); return this; }
    public DropdownBuilder option(Component label)   { options.add(label);                    return this; }
    public DropdownBuilder options(List<String> list){ list.forEach(s -> options.add(Component.literal(s))); return this; }
    public DropdownBuilder selected(int index)       { this.selectedIndex = index;             return this; }
    public DropdownBuilder action(String actionId)   { this.actionId = actionId;               return this; }

    @Override
    public DropdownComponent build() {
        return applyTooltip(new DropdownComponent(id, visible, enabled, preferredSize,
                options, selectedIndex, actionId));
    }
}