package me.lyamray.bazzaarflipper.handlers.steps.handler;

import me.lyamray.bazzaarflipper.BazaarflipperClient;
import me.lyamray.bazzaarflipper.mixin.client.HandledScreenAccessor;
import me.lyamray.bazzaarflipper.utils.random.RandomLong;
import me.lyamray.bazzaarflipper.utils.timer.TimerUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public abstract class BaseSteps {

    protected ScreenHandler getScreenHandler(MinecraftClient client) {
        if (client.currentScreen instanceof HandledScreen screen) {
            return ((HandledScreenAccessor) screen).getHandler();
        }
        return null;
    }

    protected void runDelayed(MinecraftClient client, long delay, FlipperAction action) {
        TimerUtil.getInstance().runTaskLater(() -> action.execute(client), delay);
    }

    protected long generateDelay() {
        return RandomLong.generateRandomLong();
    }

    protected Slot getSlot(ScreenHandler handler, int index) {
        if (handler == null) return null;
        return handler.getSlot(index);
    }

    protected String getMode() {
        return BazaarflipperClient.getInstance().getSellOrOrder();
    }
}