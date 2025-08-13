package me.lyamray.bazzaarflipper.handlers.slot;

import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class ClickSlotHandler {

    @Getter
    private static ClickSlotHandler instance = new ClickSlotHandler();

    public void clickSpecificSlot(MinecraftClient client, int slot, ScreenHandler screenHandler) {
        if (client.interactionManager == null) return;
        client.interactionManager.clickSlot(screenHandler.syncId, slot, 0, SlotActionType.PICKUP, client.player);
    }
}
