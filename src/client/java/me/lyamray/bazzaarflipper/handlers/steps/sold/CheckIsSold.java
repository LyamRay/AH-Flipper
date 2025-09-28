/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.handlers.steps.sold;

import lombok.Getter;
import me.lyamray.bazzaarflipper.BazaarflipperClient;
import me.lyamray.bazzaarflipper.handlers.slot.ClickSlotHandler;
import me.lyamray.bazzaarflipper.handlers.steps.handler.BaseSteps;
import me.lyamray.bazzaarflipper.handlers.steps.shared.SharedSteps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;

public class CheckIsSold extends BaseSteps {

    @Getter
    private static final CheckIsSold instance = new CheckIsSold();

    private static final int CHECK_SLOT = 10;
    private static final int RESELL_SLOT = 13;

    public void checkIfGemIsFilled(final MinecraftClient client, final ScreenHandler handler) {
        if (!(client.currentScreen instanceof HandledScreen<?>)) return;
        if (client.player == null) return;

        runDelayed(() -> {
            final BazaarflipperClient bazaar = BazaarflipperClient.getInstance();
            if (bazaar.isSold()) {
                gemSold(client, handler);
            } else {
                redoSellLogic(client, handler);
            }
            bazaar.setFilled(false);
            bazaar.setSold(false);
        }, generateDelay());
    }

    private void gemSold(final MinecraftClient client, final ScreenHandler handler) {
        final long firstDelay = generateDelay();
        final long secondDelay = firstDelay + generateDelay();

        runDelayed(() -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(client, CHECK_SLOT, handler);
            String message = "<color:#c9ffe2>Je hebt succesvol een gem gesold!</color>";
            me.lyamray.bazzaarflipper.utils.messages.MessageUtil.sendMessage(
                    me.lyamray.bazzaarflipper.utils.messages.MessageUtil.makeComponent(message)
            );
        }, firstDelay);

        runDelayed(() -> {
            final long delay = generateDelay();
            SharedSteps.getInstance().performBazaarCommand(client, delay);
            SharedSteps.getInstance().miningLogic(client, delay + generateDelay(), "order");
        }, secondDelay);
    }

    private void redoSellLogic(final MinecraftClient client, final ScreenHandler handler) {
        final long firstDelay = generateDelay();
        final long secondDelay = firstDelay + generateDelay();
        final long thirdDelay = secondDelay + generateDelay();

        runDelayed(() -> ClickSlotHandler.getInstance().clickSlotSimulated(client, CHECK_SLOT, handler),
                firstDelay);

        runDelayed(() -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(client, RESELL_SLOT, handler);
            SharedSteps.getInstance().closeInventory(client);
        }, secondDelay);

        runDelayed(() -> {
            final long delay = generateDelay();
            SharedSteps.getInstance().performBazaarCommand(client, delay);
            SharedSteps.getInstance().miningLogic(client, delay, "sell");
        }, thirdDelay);
    }
}
