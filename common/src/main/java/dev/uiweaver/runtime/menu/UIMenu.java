package dev.uiweaver.runtime.menu;

import dev.uiweaver.api.component.SlotGridComponent;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.spec.UIContextSpec;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.api.slot.SlotDescriptor;
import dev.uiweaver.runtime.action.ActionDispatcher;
import dev.uiweaver.runtime.context.ContextValidator;
import dev.uiweaver.runtime.network.ActionPacket;
import dev.uiweaver.runtime.network.NetworkChannel;
import dev.uiweaver.runtime.network.SyncPacket;
import dev.uiweaver.runtime.slot.FilteredSlot;
import dev.uiweaver.runtime.sync.SyncManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIMenu extends AbstractContainerMenu {

    private static final Logger LOGGER = LoggerFactory.getLogger(UIMenu.class);

    private final UIScreenSpec spec;
    private final SyncManager syncManager;
    private final ActionDispatcher dispatcher;
    private final NetworkChannel channel;
    private final Inventory playerInventory;
    private final int machineSlotCount;
    private BlockPos openPos;

    public UIMenu(MenuType<?> type, int containerId, Inventory playerInventory,
                  UIScreenSpec spec, NetworkChannel channel) {
        super(type, containerId);
        this.playerInventory = playerInventory;
        this.spec = spec;
        this.channel = channel;
        this.syncManager = new SyncManager(spec.getSyncs());
        this.dispatcher = new ActionDispatcher(spec.getScreenId(), spec.getActions(), spec.getContextSpec());

        registerSlotsFromSpec(spec.getRoot());
        this.machineSlotCount = slots.size();
        addPlayerInventorySlots(playerInventory);

        // force full sync on open so client doesn't start with empty values
        this.syncManager.forceFullSync();
    }

    private void registerSlotsFromSpec(UIComponent component) {
        if (component instanceof SlotGridComponent grid) {
            Container container = new SimpleContainer(grid.getSlots().size());
            for (SlotDescriptor descriptor : grid.getSlots()) {
                addSlot(new FilteredSlot(container, descriptor));
            }
        }
        for (UIComponent child : component.getChildren()) {
            registerSlotsFromSpec(child);
        }
    }

    private void addPlayerInventorySlots(Inventory inventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, col + row * 9 + 9, 0, 0));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inventory, col, 0, 0));
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (!(playerInventory.player instanceof ServerPlayer serverPlayer)) {
            LOGGER.debug("[UIWeaver] broadcastChanges skipped — not ServerPlayer ({})",
                    playerInventory.player == null ? "null" : playerInventory.player.getClass().getSimpleName());
            return;
        }

        syncManager.tick();
        if (syncManager.hasPending()) {
            SyncPacket packet = new SyncPacket(spec.getScreenId(), syncManager.drainPending());
            LOGGER.debug("[UIWeaver] Sending SyncPacket for '{}' with {} entries",
                    spec.getScreenId(), packet.getEntries().size());
            channel.sendToClient(serverPlayer, packet);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (index < machineSlotCount) {
            if (!moveItemStackTo(stack, machineSlotCount, slots.size(), true)) return ItemStack.EMPTY;
        } else {
            if (!moveItemStackTo(stack, 0, machineSlotCount, false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        UIContextSpec ctx = spec.getContextSpec();
        if (ctx.getType() == UIContextSpec.ContextType.BLOCK_ENTITY && openPos != null
                && player instanceof ServerPlayer sp) {
            return ContextValidator.validate(sp, openPos, ctx) == ContextValidator.Result.OK;
        }
        return true;
    }

    public void handleAction(ActionPacket packet, ServerPlayer player) {
        dispatcher.dispatch(packet, player);
    }

    public void forceFullSync() {
        syncManager.forceFullSync();
    }

    public UIScreenSpec getSpec() { return spec; }
    public BlockPos getOpenPos() { return openPos; }
    public void setOpenPos(BlockPos pos) { this.openPos = pos; }
}