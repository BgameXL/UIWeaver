package dev.uiweaver.client.screen;

import dev.uiweaver.api.spec.UIContextSpec;
import dev.uiweaver.runtime.network.ActionPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ScreenActionSender {

    public static void send(String screenId, String actionId, UIContextSpec contextSpec) {
        BlockPos pos = resolvePos(contextSpec);
        ActionPacket packet = new ActionPacket(screenId, actionId, pos);
        ClientNetworkBridge.sendToServer(packet);
    }

    private static BlockPos resolvePos(UIContextSpec contextSpec) {
        if (contextSpec.getType() != UIContextSpec.ContextType.BLOCK_ENTITY) return null;
        var mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen == null) return null;
        if (mc.player.containerMenu instanceof dev.uiweaver.runtime.menu.UIMenu menu) {
            var spec = menu.getSpec();
            // pos is stored in the menu when opened via NetworkHooks
            return menu.getOpenPos();
        }
        return null;
    }
}