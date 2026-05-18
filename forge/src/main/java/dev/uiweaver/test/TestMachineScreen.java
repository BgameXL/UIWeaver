package dev.uiweaver.test;

import dev.uiweaver.api.UIBuilder;
import dev.uiweaver.api.UIScreen;
import dev.uiweaver.api.action.ActionContext;
import dev.uiweaver.api.spec.UIContextSpec;
import dev.uiweaver.api.spec.UIScreenSpec;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TestMachineScreen implements UIScreen {

    public static final String ID = "uiweaver_test:machine";

    private final Supplier<Long> energy;
    private final Supplier<Long> maxEnergy;
    private final Supplier<Boolean> working;
    private final Consumer<ActionContext> onStart;
    private final Consumer<ActionContext> onStop;
    private final UIContextSpec context;

    public TestMachineScreen(Supplier<Long> energy, Supplier<Long> maxEnergy,
                             Supplier<Boolean> working,
                             Consumer<ActionContext> onStart, Consumer<ActionContext> onStop,
                             UIContextSpec context) {
        this.energy = energy;
        this.maxEnergy = maxEnergy;
        this.working = working;
        this.onStart = onStart;
        this.onStop = onStop;
        this.context = context;
    }

    public static TestMachineScreen clientSide() {
        return new TestMachineScreen(
                () -> 0L, () -> 1L, () -> false,
                ctx -> {}, ctx -> {},
                UIContextSpec.NONE
        );
    }

    @Override
    public UIScreenSpec build(UIBuilder ui) {
        return ui
                .context(context)
                .syncLong("energy",    energy)
                .syncLong("maxEnergy", maxEnergy)
                .syncBoolean("working", working)
                .action("start", onStart)
                .action("stop",  onStop)

                .root(ui.column().padding(8).gap(4).size(176, 200)

                        .add(ui.label(Component.literal("Test Machine"))
                                .id("title").fillWidth().height(10))

                        .add(ui.row().gap(6).fillWidth().height(54)

                                .add(ui.energyBar().id("energyBar")
                                        .bind("energy", "maxEnergy")
                                        .size(8, 52))

                                .add(ui.column().gap(4).grow().height(52)

                                        .add(ui.button(Component.literal("Start"))
                                                .id("btnStart").action("start")
                                                .fillWidth().height(20).minWidth(40))

                                        .add(ui.button(Component.literal("Stop"))
                                                .id("btnStop").action("stop")
                                                .fillWidth().height(20).minWidth(40))
                                )
                        )

                        .add(ui.label(Component.literal("Items"))
                                .id("invLabel").fillWidth().height(10))

                        .add(ui.slotGrid(3, 3).id("slots"))
                )
                .build();
    }
}