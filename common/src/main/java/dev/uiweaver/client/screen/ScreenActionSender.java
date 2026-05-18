package dev.uiweaver.client.screen;

import dev.uiweaver.runtime.network.ActionPacket;

public interface ScreenActionSender {

    void send(ActionPacket packet);
}