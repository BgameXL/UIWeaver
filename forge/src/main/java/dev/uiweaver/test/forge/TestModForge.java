package dev.uiweaver.test.forge;

import dev.uiweaver.runtime.registry.UIRegistry;
import dev.uiweaver.test.TestMachineBlockEntity;
import dev.uiweaver.test.TestMachineScreen;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod("uiweaver_test")
public class TestModForge {

    private static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, "uiweaver_test");
    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "uiweaver_test");
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "uiweaver_test");

    public static final RegistryObject<Block> MACHINE =
            BLOCKS.register("test_machine", TestMachineBlock::new);

    public static final RegistryObject<Item> MACHINE_ITEM =
            ITEMS.register("test_machine", () ->
                    new BlockItem(MACHINE.get(), new Item.Properties()));

    public static final RegistryObject<BlockEntityType<TestMachineBlockEntity>> MACHINE_BE =
            BLOCK_ENTITIES.register("test_machine", () ->
                    BlockEntityType.Builder.of(TestMachineBlockEntity::new, MACHINE.get()).build(null));

    public TestModForge() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        bus.addListener(this::commonSetup);

        UIRegistry.register(TestMachineScreen.ID, ui -> TestMachineScreen.clientSide().build(ui));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> TestMachineBlockEntity.TYPE = MACHINE_BE.get());
    }
}