/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.handlers.steps.shared;

import lombok.Getter;
import me.lyamray.bazzaarflipper.handlers.slot.ClickSlotHandler;
import me.lyamray.bazzaarflipper.handlers.steps.filled.CheckIsFilled;
import me.lyamray.bazzaarflipper.handlers.steps.handler.AbstractGemStep;
import me.lyamray.bazzaarflipper.handlers.steps.order.OrderSteps;
import me.lyamray.bazzaarflipper.handlers.steps.sell.SellSteps;
import me.lyamray.bazzaarflipper.handlers.steps.sold.CheckIsSold;
import me.lyamray.bazzaarflipper.mixin.client.HandledScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.ScreenHandler;

@Getter
public class SharedSteps extends AbstractGemStep {

    @Getter
    private static final SharedSteps instance = new SharedSteps();

    private static final int MINING_SLOT = 9;
    private static final int GEM_SLOT = 32;
    private static final int BEST_GEM_SLOT = 15;
    private static final int ALWAYS_TOP_SLOT = 12;
    private static final int MANAGE_ORDERS_SLOT = 50;

    private static final long MANAGE_ORDERS_DELAY = 120_000L;
    private static final long CLOSE_INVENTORY_DELAY = 1_000L;

    private void withScreenHandler(MinecraftClient client, RunnableWithHandler action) {
        final ScreenHandler handler = getScreenHandler(client);
        if (handler != null) action.run(handler);
    }

    private void handleGemMode(String mode, Runnable orderAction, Runnable sellAction) {
        switch (mode) {
            case "order" -> orderAction.run();
            case "sell" -> sellAction.run();
        }
    }

    public void clickMiningSlot(final MinecraftClient client, final ScreenHandler screenHandler, final String mode) {
        runDelayed(() -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(client, MINING_SLOT, screenHandler);
            clickGemSlot(client, mode);
        }, generateDelay());
    }

    public void clickGemSlot(final MinecraftClient client, final String mode) {
        withScreenHandler(client, handler ->
                runDelayed(() -> {
                    ClickSlotHandler.getInstance().clickSlotSimulated(client, GEM_SLOT, handler);
                    handleGemMode(mode,
                            () -> OrderSteps.getInstance().chooseWhichGem(client),
                            () -> SellSteps.getInstance().chooseWhichGem(client));
                }, generateDelay())
        );
    }

    public void clickBestGemSlot(final MinecraftClient client, final String mode) {
        withScreenHandler(client, handler ->
                runDelayed(() -> {
                    ClickSlotHandler.getInstance().clickSlotSimulated(client, BEST_GEM_SLOT, handler);
                    handleGemMode(mode,
                            () -> OrderSteps.getInstance().createBuyOrder(client),
                            () -> SellSteps.getInstance().createSellOrder(client));
                }, generateDelay())
        );
    }

    public void alwaysBeOnTop(final MinecraftClient client, final String mode) {
        withScreenHandler(client, handler ->
                runDelayed(() -> {
                    ClickSlotHandler.getInstance().clickSlotSimulated(client, ALWAYS_TOP_SLOT, handler);
                    handleGemMode(mode,
                            () -> OrderSteps.getInstance().confirmOrder(client),
                            () -> SellSteps.getInstance().confirmOrder(client));
                }, generateDelay())
        );
    }

    public void manageOrders(final MinecraftClient client, final String mode) {
        final ScreenHandler handler = getScreenHandler(client);
        if (handler == null) {
            runDelayed(() -> manageOrders(client, mode), generateDelay());
            return;
        }

        runDelayed(() -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(client, MANAGE_ORDERS_SLOT, handler);
            handleGemMode(mode,
                    () -> CheckIsFilled.getInstance().checkIfGemIsFilled(client, handler),
                    () -> CheckIsSold.getInstance().checkIfGemIsFilled(client, handler));
        }, MANAGE_ORDERS_DELAY);
    }

    public void performBazaarCommand(final MinecraftClient client, final long delay) {
        if (client.player == null || client.player.networkHandler == null) return;
        runDelayed(() -> client.getNetworkHandler().sendChatCommand("baz"), delay);
    }

    public void miningLogic(final MinecraftClient client, final long random, final String mode) {
        runDelayed(() -> withScreenHandler(client, handler -> clickMiningSlot(client, handler, mode)), random + 1000L);
    }

    public void closeInventory(final MinecraftClient client) {
        runDelayed(() -> {
            if (client.player != null && client.currentScreen instanceof HandledScreenAccessor) {
                client.player.closeHandledScreen();
                client.setScreen(null);
            }
        }, CLOSE_INVENTORY_DELAY);
    }

    @FunctionalInterface
    private interface RunnableWithHandler {
        void run(ScreenHandler handler);
    }
}
