package dev.uiweaver.api.component;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface Tooltipable {

    List<Component> getTooltip();
}