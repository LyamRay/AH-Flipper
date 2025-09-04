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
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Hand;

@Getter
public class SharedSteps extends AbstractGemStep {

    @Getter
    private static SharedSteps instance = new SharedSteps();

    public void clickMiningSlot(MinecraftClient client, ScreenHandler screenHandler) {
        runDelayed(() -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(client, 9, screenHandler);
            clickGemSlot(client);
        }, generateDelay());
    }

    public void clickGemSlot(MinecraftClient client) {
        ScreenHandler screenHandler = getScreenHandler(client);
        if (screenHandler == null) return;

        String mode = getMode();
        if (mode == null) return;

        runDelayed(() -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(client, 32, screenHandler);
            switch (mode) {
                case "order" -> OrderSteps.getInstance().chooseWhichGem(client);
                case "sell" -> SellSteps.getInstance().chooseWhichGem(client);
            }
        }, generateDelay());
    }

    public void clickBestGemSlot(MinecraftClient client) {
        ScreenHandler screenHandler = getScreenHandler(client);
        if (screenHandler == null) return;

        String mode = getMode();
        if (mode == null) return;

        runDelayed(() -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(client, 15, screenHandler);
            switch (mode) {
                case "order" -> OrderSteps.getInstance().createBuyOrder(client);
                case "sell" -> SellSteps.getInstance().createSellOrder(client);
            }
        }, generateDelay());
    }

    public void alwaysBeOnTop(MinecraftClient client) {
        ScreenHandler screenHandler = getScreenHandler(client);
        if (screenHandler == null) return;

        String mode = getMode();
        if (mode == null) return;

        runDelayed(() -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(client, 12, screenHandler);
            switch (mode) {
                case "order" -> OrderSteps.getInstance().confirmOrder(client);
                case "sell" -> SellSteps.getInstance().confirmOrder(client);
            }
        }, generateDelay());
    }

    public void manageOrders(MinecraftClient client) {
        ScreenHandler screenHandler = getScreenHandler(client);
        if (screenHandler == null) return;

        String mode = getMode();
        if (mode == null) return;

        long threeMinutes = 20000; //not 3 mins for testing

        runDelayed(() -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(client, 50, screenHandler);
            switch (mode) {
                case "order" -> CheckIsFilled.getInstance().checkIfGemIsFilled(client, screenHandler);
                case "sell" -> CheckIsSold.getInstance().checkIfGemIsFilled(client, screenHandler);
            }
        }, threeMinutes);
    }

    public void performBazaarCommand(MinecraftClient client, long random) {
        if (client.player.networkHandler == null) return;

        runDelayed(() -> client.getNetworkHandler().sendChatCommand("baz"), random);
    }

    public void miningLogic(MinecraftClient client, long random) {
        runDelayed(() -> {
            ScreenHandler handler = getScreenHandler(client);
            if (handler != null) clickMiningSlot(client, handler);
        }, random + 500);
    }

    public void leftClickPacket(MinecraftClient client) {
        if (client.interactionManager == null || client.targetedEntity == null) return;

        Entity entity = client.targetedEntity;

        runDelayed(() -> {
            client.player.swingHand(Hand.MAIN_HAND);
            client.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
            client.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(entity, client.player.isSneaking()));

            ScreenHandler handler = getScreenHandler(client);
            if (handler != null) clickMiningSlot(client, handler);
        }, generateDelay());
    }

    public void closeInventory(MinecraftClient client) {
        runDelayed(() -> {
            if (client.player != null) {
                client.player.closeHandledScreen();
            }
            client.setScreen(null);
        }, 1000L);
    }
}
