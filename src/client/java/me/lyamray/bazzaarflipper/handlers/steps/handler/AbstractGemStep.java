/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.handlers.steps.handler;

import me.lyamray.bazzaarflipper.handlers.slot.ClickSlotHandler;
import me.lyamray.bazzaarflipper.utils.messages.Gems;
import me.lyamray.bazzaarflipper.utils.timer.TimerUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.ScreenHandler;

public abstract class AbstractGemStep extends BaseSteps {

    protected int getGemSlot(String gemName) {
        try {
            return Gems.valueOf(gemName.toUpperCase()).getSlot();
        } catch (IllegalArgumentException e) {
            return -1;
        }
    }

    protected void clickSlot(MinecraftClient client, int slotIndex, long delay, Runnable nextTask) {
        ScreenHandler handler = getScreenHandler(client);
        if (handler == null) return;

        TimerUtil.getInstance().runTaskLater(() -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(client, slotIndex, handler);
            if (nextTask != null) nextTask.run();
        }, delay);
    }
}
