package dev.uiweaver.test.fabric;

import dev.uiweaver.runtime.registry.UIRegistry;
import dev.uiweaver.test.TestMachineScreen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TestModFabric implements ModInitializer {

    public static Block MACHINE;
    public static BlockEntityType<TestMachineBlockEntityFabric> MACHINE_BE;
    public static Item MACHINE_ITEM;

    @Override
    public void onInitialize() {
        MACHINE = Registry.register(
                BuiltInRegistries.BLOCK,
                new ResourceLocation("uiweaver_test", "test_machine"),
                new TestMachineBlockFabric()
        );

        MACHINE_ITEM = Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation("uiweaver_test", "test_machine"),
                new BlockItem(MACHINE, new Item.Properties())
        );

        MACHINE_BE = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation("uiweaver_test", "test_machine"),
                FabricBlockEntityTypeBuilder.create(TestMachineBlockEntityFabric::new, MACHINE).build()
        );

        TestMachineBlockEntityFabric.TYPE = MACHINE_BE;

        UIRegistry.register(TestMachineScreen.ID, ui -> TestMachineScreen.clientSide().build(ui));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.OP_BLOCKS)
                .register(entries -> entries.accept(MACHINE_ITEM));
    }
}