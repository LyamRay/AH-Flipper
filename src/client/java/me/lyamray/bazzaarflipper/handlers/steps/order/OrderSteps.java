/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.handlers.steps.order;

import lombok.Getter;
import me.lyamray.bazzaarflipper.BazaarflipperClient;
import me.lyamray.bazzaarflipper.handlers.steps.handler.AbstractGemStep;
import me.lyamray.bazzaarflipper.handlers.steps.shared.SharedSteps;
import me.lyamray.bazzaarflipper.utils.messages.MessageUtil;
import net.minecraft.client.MinecraftClient;

public class OrderSteps extends AbstractGemStep {

    @Getter
    private static final OrderSteps instance = new OrderSteps();

    private static final long RANDOM_DELAY = getInstance().generateDelay();

    private static final int BEST_GEM_SLOT = 15;
    private static final int BUY_ONE_GEM_SLOT = 10;
    private static final int CONFIRM_ORDER_SLOT = 13;

    public void chooseWhichGem(final MinecraftClient client) {
        final int slot = getGemSlot(BazaarflipperClient.getInstance().getGem());
        if (slot == -1) return;

        clickSlot(client, slot, generateDelay(),
                () -> SharedSteps.getInstance().clickBestGemSlot(client, "order"));
    }

    public void createBuyOrder(final MinecraftClient client) {
        clickSlot(client, BEST_GEM_SLOT, generateDelay(),
                () -> buyOneGem(client));
    }

    public void buyOneGem(final MinecraftClient client) {
        clickSlot(client, BUY_ONE_GEM_SLOT, generateDelay(),
                () -> SharedSteps.getInstance().alwaysBeOnTop(client, "order"));
    }

    public void confirmOrder(final MinecraftClient client) {
        final long delay = generateDelay();

        clickSlot(client, CONFIRM_ORDER_SLOT, delay, () -> {
            String message = "<color:#c9ffe2>Je hebt succesvol een buy-order geplaatst!</color>";
            MessageUtil.sendMessage(MessageUtil.makeComponent(message));
            SharedSteps.getInstance().performBazaarCommand(client, RANDOM_DELAY);
        });

        runDelayed(() -> SharedSteps.getInstance().manageOrders(client, "order"),
                delay + generateDelay());
    }
}
