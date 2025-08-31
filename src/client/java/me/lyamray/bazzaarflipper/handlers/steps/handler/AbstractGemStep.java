package me.lyamray.bazzaarflipper.handlers.steps.handler;

import me.lyamray.bazzaarflipper.handlers.slot.ClickSlotHandler;
import me.lyamray.bazzaarflipper.utils.messages.Gems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.ScreenHandler;

import java.util.function.Consumer;

public abstract class AbstractGemStep extends BaseSteps {

    protected int getGemSlot(String gemName) {
        try {
            return Gems.valueOf(gemName.toUpperCase()).getSlot();
        } catch (IllegalArgumentException e) {
            return -1;
        }
    }

    protected void clickSlot(MinecraftClient client, int slotIndex, FlipperAction nextAction) {
        ScreenHandler handler = getScreenHandler(client);
        if (handler == null) return;

        runDelayed(client, generateDelay(), c -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(c, slotIndex, handler);
            if (nextAction != null) nextAction.execute(c);
        });
    }

    protected void executeByMode(MinecraftClient client, Consumer<String> orderAction, Consumer<String> sellAction) {
        String mode = getMode();
        if (mode == null) return;

        switch (mode) {
            case "order" -> orderAction.accept(mode);
            case "sell" -> sellAction.accept(mode);
        }
    }
}