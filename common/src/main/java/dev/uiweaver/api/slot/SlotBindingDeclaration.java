package dev.uiweaver.api.slot;

import net.minecraft.world.Container;

import java.util.function.Supplier;

public class SlotBindingDeclaration {

    private final String name;
    private final Supplier<Container> source;

    private SlotBindingDeclaration(String name, Supplier<Container> source) {
        this.name = name;
        this.source = source;
    }

    public static SlotBindingDeclaration of(String name, Supplier<Container> source) {
        return new SlotBindingDeclaration(name, source);
    }

    public String getName() { return name; }
    public Supplier<Container> getSource() { return source; }
}