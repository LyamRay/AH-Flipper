/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

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

    protected void runDelayed(Runnable task, long delay) {
        TimerUtil.getInstance().runTaskLater(task, delay);
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
