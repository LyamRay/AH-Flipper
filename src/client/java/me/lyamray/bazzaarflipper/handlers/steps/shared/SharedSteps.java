package me.lyamray.bazzaarflipper.handlers.steps.shared;

import lombok.Getter;
import me.lyamray.bazzaarflipper.handlers.slot.ClickSlotHandler;
import me.lyamray.bazzaarflipper.handlers.steps.filled.CheckIsFilled;
import me.lyamray.bazzaarflipper.handlers.steps.handler.AbstractGemStep;
import me.lyamray.bazzaarflipper.handlers.steps.order.OrderSteps;
import me.lyamray.bazzaarflipper.handlers.steps.sell.SellSteps;
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
        runDelayed(client, generateDelay(), c -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(c, 9, screenHandler);
            clickGemSlot(c);
        });
    }

    public void clickGemSlot(MinecraftClient client) {
        ScreenHandler screenHandler = getScreenHandler(client);
        if (screenHandler == null) return;

        String mode = getMode();
        if (mode == null) return;

        runDelayed(client, generateDelay(), c -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(c, 32, screenHandler);
            switch (mode) {
                case "order" -> OrderSteps.getInstance().chooseWhichGem(c);
                case "sell" -> SellSteps.getInstance().chooseWhichGem(c);
            }
        });
    }

    public void clickBestGemSlot(MinecraftClient client) {
        ScreenHandler screenHandler = getScreenHandler(client);
        if (screenHandler == null) return;

        String mode = getMode();
        if (mode == null) return;

        runDelayed(client, generateDelay(), c -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(c, 15, screenHandler);
            switch (mode) {
                case "order" -> OrderSteps.getInstance().createBuyOrder(c);
                case "sell" -> SellSteps.getInstance().createSellOrder(c);
            }
        });
    }

    public void alwaysBeOnTop(MinecraftClient client) {
        ScreenHandler screenHandler = getScreenHandler(client);
        if (screenHandler == null) return;

        executeByMode(client,
                m -> runDelayed(client, generateDelay(), c -> ClickSlotHandler.getInstance().clickSlotSimulated(c, 12, screenHandler)),
                m -> runDelayed(client, generateDelay(), c -> ClickSlotHandler.getInstance().clickSlotSimulated(c, 12, screenHandler))
        );
    }

    public void manageOrders(MinecraftClient client) {
        ScreenHandler screenHandler = getScreenHandler(client);
        if (screenHandler == null) return;

        String mode = getMode();
        if (mode == null) return;

        runDelayed(client, generateDelay(), c -> {
            ClickSlotHandler.getInstance().clickSlotSimulated(c, 50, screenHandler);
            switch (mode) {
                case "order" -> CheckIsFilled.getInstance().checkIfGemIsFilled(c, screenHandler);
                case "sell" -> SellSteps.getInstance().confirmOrder(c);
            }
        });
    }

    public void performBazaarCommand(MinecraftClient client) {
        if (client.player.networkHandler == null) return;

        long random = generateDelay();
        runDelayed(client, random, c -> c.getNetworkHandler().sendChatCommand("baz"));
        runDelayed(client, random + 500, c -> {
            ScreenHandler handler = getScreenHandler(c);
            if (handler != null) clickMiningSlot(c, handler);
        });
    }

    public void leftClickPacket(MinecraftClient client) {
        if (client.interactionManager == null || client.targetedEntity == null) return;

        Entity entity = client.targetedEntity;

        runDelayed(client, generateDelay(), c -> {
            c.player.swingHand(Hand.MAIN_HAND);
            c.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
            c.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(entity, c.player.isSneaking()));

            ScreenHandler handler = getScreenHandler(c);
            if (handler != null) clickMiningSlot(c, handler);
        });
    }

    public void closeInventory(MinecraftClient client) {
        runDelayed(client, 1000L, c -> {
            if (c.currentScreen != null) c.currentScreen.close();
        });
    }
}
