package dev.uiweaver.example;

import dev.uiweaver.api.UIBuilder;
import dev.uiweaver.api.UIScreen;
import dev.uiweaver.api.context.UIContextPayload;
import dev.uiweaver.api.spec.UIScreenSpec;
import net.minecraft.network.chat.Component;

public class ExampleMachineScreen implements UIScreen {

    private final ExampleMachineBlockEntity machine;
    private final UIContextPayload context;

    public ExampleMachineScreen(ExampleMachineBlockEntity machine, UIContextPayload context) {
        this.machine = machine;
        this.context = context;
    }

    @Override
    public UIScreenSpec build(UIBuilder ui) {
        return ui
                .context(context)

                .syncLong("energy",    machine::getEnergy)
                .syncLong("maxEnergy", machine::getMaxEnergy)
                .syncBoolean("working", machine::isWorking)

                .action("start", ctx -> ctx.blockEntity(ExampleMachineBlockEntity.class).start())
                .action("stop",  ctx -> ctx.blockEntity(ExampleMachineBlockEntity.class).stop())

                .root(ui.column().padding(8).gap(4)
                        .add(ui.label(Component.translatable("gui.example.machine_title")))
                        .add(ui.row().gap(6)
                                .add(ui.energyBar().id("energyBar").bind("energy", "maxEnergy").size(8, 52))
                                .add(ui.column().gap(4)
                                        .add(ui.button("Start").id("btnStart").action("start").size(60, 16))
                                        .add(ui.button("Stop").id("btnStop").action("stop").size(60, 16))
                                )
                        )
                )

                .build();
    }
}