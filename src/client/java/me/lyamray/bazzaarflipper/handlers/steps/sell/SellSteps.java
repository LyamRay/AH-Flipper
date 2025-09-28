/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.handlers.steps.sell;

import lombok.Getter;
import me.lyamray.bazzaarflipper.BazaarflipperClient;
import me.lyamray.bazzaarflipper.handlers.steps.handler.AbstractGemStep;
import me.lyamray.bazzaarflipper.handlers.steps.shared.SharedSteps;
import me.lyamray.bazzaarflipper.utils.messages.MessageUtil;
import net.minecraft.client.MinecraftClient;

public class SellSteps extends AbstractGemStep {

    @Getter
    private static final SellSteps instance = new SellSteps();

    private static final long RANDOM_DELAY = getInstance().generateDelay();

    private static final int CREATE_SELL_ORDER_SLOT = 16;
    private static final int BEST_OFFER_MINUS_ONE_SLOT = 12;
    private static final int CONFIRM_ORDER_SLOT = 13;

    public void chooseWhichGem(final MinecraftClient client) {
        final int slot = getGemSlot(BazaarflipperClient.getInstance().getGem());
        if (slot == -1) return;

        clickSlot(client, slot, generateDelay(),
                () -> SharedSteps.getInstance().clickBestGemSlot(client, "sell"));
    }

    public void createSellOrder(final MinecraftClient client) {
        clickSlot(client, CREATE_SELL_ORDER_SLOT, generateDelay(),
                () -> bestOfferMinusOne(client));
    }

    public void bestOfferMinusOne(final MinecraftClient client) {
        clickSlot(client, BEST_OFFER_MINUS_ONE_SLOT, generateDelay(),
                () -> SharedSteps.getInstance().alwaysBeOnTop(client, "sell"));
    }

    public void confirmOrder(final MinecraftClient client) {
        final long delay = generateDelay();

        clickSlot(client, CONFIRM_ORDER_SLOT, delay, () -> {
            String message = "<color:#c9ffe2>Je hebt succesvol een sell-offer geplaatst!</color>";
            MessageUtil.sendMessage(MessageUtil.makeComponent(message));
            SharedSteps.getInstance().performBazaarCommand(client, RANDOM_DELAY);
        });

        runDelayed(() -> SharedSteps.getInstance().manageOrders(client, "sell"),
                delay + generateDelay());
    }
}
