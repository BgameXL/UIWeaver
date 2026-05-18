package dev.uiweaver.test;

import dev.uiweaver.api.UIBuilder;
import dev.uiweaver.api.UIScreen;
import dev.uiweaver.api.action.ActionContext;
import dev.uiweaver.api.component.ProgressBarComponent;
import dev.uiweaver.api.context.UIContextPayload;
import dev.uiweaver.api.spec.UIScreenSpec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TestMachineScreen implements UIScreen {

    public static final String ID = "uiweaver_test:machine";

    private final Supplier<Long>         energy;
    private final Supplier<Long>         maxEnergy;
    private final Supplier<Boolean>      working;
    private final Supplier<Container>    inventory;
    private final Consumer<ActionContext> onStart;
    private final Consumer<ActionContext> onStop;
    private final UIContextPayload        context;

    public TestMachineScreen(Supplier<Long> energy, Supplier<Long> maxEnergy,
                             Supplier<Boolean> working, Supplier<Container> inventory,
                             Consumer<ActionContext> onStart, Consumer<ActionContext> onStop,
                             UIContextPayload context) {
        this.energy    = energy;
        this.maxEnergy = maxEnergy;
        this.working   = working;
        this.inventory = inventory;
        this.onStart   = onStart;
        this.onStop    = onStop;
        this.context   = context;
    }

    public static TestMachineScreen clientSide() {
        return new TestMachineScreen(
                () -> 0L, () -> 1L, () -> false, () -> null,
                ctx -> {}, ctx -> {},
                UIContextPayload.NONE
        );
    }

    @Override
    public UIScreenSpec build(UIBuilder ui) {
        return ui
                .context(context)
                .syncLong("energy", energy)
                .syncLong("maxEnergy", maxEnergy)
                .syncBoolean("working", working)
                .action("start", onStart)
                .action("stop", onStop)
                .slots("machineInv", inventory)

                .bind("mainProgress", "value", "energy")
                .bind("mainProgress", "maxValue", "maxEnergy")
                .bind("coreLoad", "value", "energy")
                .bind("coreLoad", "maxValue", "maxEnergy")
                .bind("heatLoad", "value", "energy")
                .bind("heatLoad", "maxValue", "maxEnergy")
                .bind("fluidReserve", "amount", "energy")
                .bind("fluidReserve", "capacity", "maxEnergy")

                .root(ui.column().padding(10).gap(8).size(530, 340)

                        .add(ui.row().gap(8).fillWidth().height(28)
                                .add(ui.column().gap(2).grow().height(28)
                                        .add(ui.label(Component.literal("UIWeaver Reactor"))
                                                .id("title").fillWidth().height(10))
                                        .add(ui.label(Component.literal("Server driven test machine"))
                                                .id("subtitle").fillWidth().height(10))
                                )
                                .add(ui.button(Component.literal("Start"))
                                        .id("btnStart").action("start").size(54, 20))
                                .add(ui.button(Component.literal("Stop"))
                                        .id("btnStop").action("stop").size(54, 20))
                        )

                        .add(ui.row().gap(8).fillWidth().height(58)
                                .add(ui.energyBar().id("energyBar")
                                        .bind("energy", "maxEnergy").size(12, 54)
                                        .tooltip("Live energy storage."))

                                .add(ui.column().gap(5).grow().height(58)
                                        .add(ui.label(Component.literal("Main cycle"))
                                                .id("cycleLabel").fillWidth().height(10))

                                        .add(ui.progressBar().id("mainProgress")
                                                .direction(ProgressBarComponent.Direction.LEFT_TO_RIGHT)
                                                .fillWidth().height(12))

                                        .add(ui.row().gap(6).fillWidth().height(26)
                                                .add(ui.column().gap(3).grow().height(26)
                                                        .add(ui.label(Component.literal("Core"))
                                                                .id("coreLoadLabel").fillWidth().height(10))
                                                        .add(ui.progressBar().id("coreLoad")
                                                                .direction(ProgressBarComponent.Direction.LEFT_TO_RIGHT)
                                                                .fillWidth().height(10))
                                                )
                                                .add(ui.column().gap(3).grow().height(26)
                                                        .add(ui.label(Component.literal("Heat"))
                                                                .id("heatLoadLabel").fillWidth().height(10))
                                                        .add(ui.progressBar().id("heatLoad")
                                                                .direction(ProgressBarComponent.Direction.LEFT_TO_RIGHT)
                                                                .fillWidth().height(10))
                                                )
                                        )
                                )

                                .add(ui.fluidBar().id("fluidReserve")
                                        .size(12, 54)
                                        .tooltip("Demo fluid reserve."))
                        )

                        .add(ui.tabs().id("mainTabs").fillWidth().grow()

                                .tab("Main", ui.column().padding(8).gap(6)

                                        .add(ui.row().gap(8).fillWidth().height(52)
                                                .add(ui.column().padding(5).gap(4).grow().height(50)
                                                        .add(ui.label(Component.literal("Machine State"))
                                                                .id("statusHeader").fillWidth().height(10))
                                                        .add(ui.label(Component.literal("Energy sync and actions are active."))
                                                                .id("statusBody").fillWidth().height(10))
                                                        .add(ui.toggle().id("statusToggle").size(26, 12))
                                                )

                                                .add(ui.column().padding(5).gap(4).grow().height(50)
                                                        .add(ui.label(Component.literal("Recipe Search"))
                                                                .id("recipeHeader").fillWidth().height(10))
                                                        .add(ui.textInput().id("recipeSearch")
                                                                .placeholder("Search...")
                                                                .fillWidth().height(16))
                                                )
                                        )

                                        .add(ui.scrollPanel().id("overviewLog")
                                                .padding(5).gap(4).fillWidth().grow()
                                                .add(ui.label(Component.literal("F-f-f-f-f-f-Funding for this program was made possible by-by-by-by-by")))
                                                .add(ui.label(Component.literal("Fun-by-by-by-by-ding by-by-by-by-for thiiiiii")))
                                                .add(ui.label(Component.literal("Program. Program. Pro-Pro-Pro-Pro-Pro-gram")))
                                                .add(ui.label(Component.literal("Funding for-by-by-made possible by viewers like you")))
                                                .add(ui.label(Component.literal("Like you. Like you. Like you. Like you. Like you. Like you")))
                                                .add(ui.label(Component.literal("Broadcast.")))
                                        )
                                )

                                .tab("Inv", ui.column().padding(8).gap(6)

                                        .add(ui.row().gap(10).fillWidth().height(70)

                                                .add(ui.column().gap(4).width(74).height(70)
                                                        .add(ui.label(Component.literal("Machine"))
                                                                .id("machineInvLabel").fillWidth().height(10))
                                                        .add(ui.slotGrid(3, 3)
                                                                .id("slots")
                                                                .bind("machineInv"))
                                                )

                                                .add(ui.column().gap(5).grow().height(70)
                                                        .add(ui.label(Component.literal("Inventory Actions"))
                                                                .id("actionsLabel").fillWidth().height(10))
                                                        .add(ui.button(Component.literal("Auto-balance"))
                                                                .id("balanceButton").fillWidth().height(18))
                                                        .add(ui.button(Component.literal("Lock layout"))
                                                                .id("lockButton").fillWidth().height(18))
                                                )
                                        )

                                        .add(ui.label(Component.literal("Player inventory hidden for now."))
                                                .id("playerHiddenNotice").fillWidth().height(10))

                                        .add(ui.label(Component.literal("Reason: slot layout needs better absolute positioning first."))
                                                .id("slotNote").fillWidth().height(10))
                                )

                                .tab("Tune", ui.column().padding(8).gap(7)

                                        .add(ui.label(Component.literal("Machine Tuning"))
                                                .id("tuningTitle").fillWidth().height(10))

                                        .add(ui.row().gap(8).fillWidth().height(18)
                                                .add(ui.label(Component.literal("Speed"))
                                                        .id("speedLabel").width(48).height(10))
                                                .add(ui.slider(0, 100)
                                                        .id("speedSlider")
                                                        .value(40)
                                                        .grow()
                                                        .height(14))
                                        )

                                        .add(ui.row().gap(8).fillWidth().height(18)
                                                .add(ui.label(Component.literal("Safety"))
                                                        .id("safetyLabel").width(48).height(10))
                                                .add(ui.slider(0, 100)
                                                        .id("safetySlider")
                                                        .value(75)
                                                        .grow()
                                                        .height(14))
                                        )

                                        .add(ui.row().gap(8).fillWidth().height(18)
                                                .add(ui.checkbox(Component.literal("Auto input"))
                                                        .id("autoInput")
                                                        .size(92, 14))
                                                .add(ui.checkbox(Component.literal("Silent"))
                                                        .id("silentMode")
                                                        .size(80, 14))
                                        )

                                        .add(ui.button(Component.literal("Apply tuning"))
                                                .id("applyTuning")
                                                .fillWidth()
                                                .height(20))
                                )
                        )
                )
                .build();
    }
}
