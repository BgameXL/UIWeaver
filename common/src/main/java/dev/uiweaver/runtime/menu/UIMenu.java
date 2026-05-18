package dev.uiweaver.runtime.menu;

import dev.uiweaver.api.component.SlotGridComponent;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.context.UIContextPayload;
import dev.uiweaver.api.slot.SlotBindingDeclaration;
import dev.uiweaver.api.slot.SlotDescriptor;
import dev.uiweaver.api.spec.UIScreenSpec;
import dev.uiweaver.runtime.action.ActionDispatcher;
import dev.uiweaver.runtime.context.ContextValidator;
import dev.uiweaver.runtime.lifecycle.SessionManager;
import dev.uiweaver.runtime.lifecycle.UIScreenSession;
import dev.uiweaver.runtime.network.ActionPacket;
import dev.uiweaver.runtime.network.NetworkChannel;
import dev.uiweaver.runtime.network.SyncPacket;
import dev.uiweaver.runtime.slot.FilteredSlot;
import dev.uiweaver.runtime.sync.SyncManager;
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

import java.util.HashMap;
import java.util.Map;

public class UIMenu extends AbstractContainerMenu {

    private static final Logger LOGGER = LoggerFactory.getLogger(UIMenu.class);

    private final UIScreenSpec spec;
    private final SyncManager syncManager;
    private final ActionDispatcher dispatcher;
    private final NetworkChannel channel;
    private final Inventory playerInventory;
    private final int machineSlotCount;
    private UIScreenSession session;
    private int clientSessionId = -1;

    public UIMenu(MenuType<?> type, int containerId, Inventory playerInventory,
                  UIScreenSpec spec, NetworkChannel channel) {
        super(type, containerId);
        this.playerInventory = playerInventory;
        this.spec            = spec;
        this.channel         = channel;
        this.syncManager     = new SyncManager(spec.getSyncs());
        this.dispatcher      = new ActionDispatcher(spec.getScreenId(), spec.getActions(), spec.getContextPayload());

        Map<String, Container> resolved = resolveSlotBindings(spec);
        registerSlotsFromSpec(spec.getRoot(), resolved);
        this.machineSlotCount = slots.size();
        addPlayerInventorySlots(playerInventory);
    }

    public void onOpened(ServerPlayer player) {
        this.session = SessionManager.open(player, new UIScreenSession(player, spec));
        syncManager.forceFullSync();
    }

    public void setClientSessionId(int id) { this.clientSessionId = id; }
    public int getClientSessionId() { return clientSessionId; }

    public int getSessionId() {
        return session != null ? session.getSessionId() : -1;
    }

    private Map<String, Container> resolveSlotBindings(UIScreenSpec spec) {
        Map<String, Container> resolved = new HashMap<>();
        for (SlotBindingDeclaration decl : spec.getSlotBindings()) {
            try {
                Container container = decl.getSource().get();
                if (container != null) resolved.put(decl.getName(), container);
                else LOGGER.warn("[UIWeaver] Slot binding '{}' returned null", decl.getName());
            } catch (Exception e) {
                LOGGER.error("[UIWeaver] Failed to resolve slot binding '{}'", decl.getName(), e);
            }
        }
        return resolved;
    }

    private void registerSlotsFromSpec(UIComponent component, Map<String, Container> containers) {
        if (component instanceof SlotGridComponent grid) {
            Container container;
            if (grid.hasBoundContainer()) {
                container = containers.get(grid.getBindingName());
                if (container == null) {
                    LOGGER.warn("[UIWeaver] SlotGrid '{}' bound to '{}' but no match — using SimpleContainer",
                            grid.getId(), grid.getBindingName());
                    container = new SimpleContainer(grid.getSlots().size());
                }
            } else {
                container = new SimpleContainer(grid.getSlots().size());
            }
            for (SlotDescriptor descriptor : grid.getSlots()) {
                addSlot(new FilteredSlot(container, descriptor));
            }
        }
        for (UIComponent child : component.getChildren()) {
            registerSlotsFromSpec(child, containers);
        }
    }

    private void addPlayerInventorySlots(Inventory inventory) {
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(inventory, col + row * 9 + 9, 0, 0));
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(inventory, col, 0, 0));
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (!(playerInventory.player instanceof ServerPlayer serverPlayer)) return;
        syncManager.tick();
        if (syncManager.hasPending()) {
            channel.sendToClient(serverPlayer, new SyncPacket(spec.getScreenId(), syncManager.drainPending()));
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (player instanceof ServerPlayer sp) SessionManager.close(sp);
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
        if (!(player instanceof ServerPlayer sp)) return true;
        UIContextPayload payload = spec.getContextPayload();
        if (payload instanceof UIContextPayload.BlockContext) {
            return ContextValidator.validate(sp, payload) == ContextValidator.Result.OK;
        }
        return true;
    }

    public void handleAction(ActionPacket packet, ServerPlayer player) {
        dispatcher.dispatch(packet, player);
    }

    public void forceFullSync() { syncManager.forceFullSync(); }

    public UIScreenSpec getSpec() { return spec; }
}