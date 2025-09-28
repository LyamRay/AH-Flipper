/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.handlers.steps.filled;

import lombok.Getter;
import me.lyamray.bazzaarflipper.BazaarflipperClient;
import me.lyamray.bazzaarflipper.handlers.slot.ClickSlotHandler;
import me.lyamray.bazzaarflipper.handlers.steps.handler.BaseSteps;
import me.lyamray.bazzaarflipper.handlers.steps.shared.SharedSteps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;

public class CheckIsFilled extends BaseSteps {

    @Getter
    private static final CheckIsFilled instance = new CheckIsFilled();

    private static final int CHECK_SLOT = 19;
    private static final int REBUY_SLOT = 11;

    public void checkIfGemIsFilled(final MinecraftClient client, final ScreenHandler handler) {
        if (!(client.currentScreen instanceof HandledScreen<?>)) return;

        runDelayed(() -> {
            final BazaarflipperClient bazaar = BazaarflipperClient.getInstance();
            if (bazaar.isFilled()) {
                doSellLogic(client, handler);
            } else {
                redoBuyLogic(client, handler);
            }
            bazaar.setFilled(false);
            bazaar.setSold(false);
        }, generateDelay());
    }

    private void doSellLogic(final MinecraftClient client, final ScreenHandler handler) {
        final long firstDelay = generateDelay();
        final long secondDelay = firstDelay + generateDelay();

        runDelayed(() -> ClickSlotHandler.getInstance().clickSlotSimulated(client, CHECK_SLOT, handler),
                firstDelay);

        runDelayed(() -> {
            SharedSteps.getInstance().closeInventory(client);
            final long delay = generateDelay();
            SharedSteps.getInstance().performBazaarCommand(client, delay);
            SharedSteps.getInstance().miningLogic(client, delay, "sell");
        }, secondDelay);
    }

    private void redoBuyLogic(final MinecraftClient client, final ScreenHandler handler) {
        final long firstDelay = generateDelay();
        final long secondDelay = firstDelay + generateDelay();
        final long thirdDelay = secondDelay + generateDelay();

        runDelayed(() -> ClickSlotHandler.getInstance().clickSlotSimulated(client, CHECK_SLOT, handler),
                firstDelay);

        runDelayed(() -> ClickSlotHandler.getInstance().clickSlotSimulated(client, REBUY_SLOT, handler),
                secondDelay);

        runDelayed(() -> {
            final long delay = generateDelay();
            SharedSteps.getInstance().closeInventory(client);
            SharedSteps.getInstance().performBazaarCommand(client, delay);
            SharedSteps.getInstance().miningLogic(client, delay, "order");
        }, thirdDelay);
    }
}
