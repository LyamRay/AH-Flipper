package me.lyamray.bazzaarflipper.handlers.steps.shared;

import lombok.Getter;
import me.lyamray.bazzaarflipper.handlers.slot.ClickSlotHandler;
import me.lyamray.bazzaarflipper.handlers.steps.filled.CheckIsFilled;
import me.lyamray.bazzaarflipper.handlers.steps.handler.AbstractGemStep;
import me.lyamray.bazzaarflipper.handlers.steps.order.OrderSteps;
import me.lyamray.bazzaarflipper.handlers.steps.sell.SellSteps;
import me.lyamray.bazzaarflipper.handlers.steps.sold.CheckIsSold;
import me.lyamray.bazzaarflipper.mixin.client.HandledScreenAccessor;
import me.lyamray.bazzaarflipper.utils.messages.MessageUtil;
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
        long delay = generateDelay();
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Scheduling clickMiningSlot in " + delay + "ms"));
        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Clicking mining slot 9"));
            ClickSlotHandler.getInstance().clickSlotSimulated(client, 9, screenHandler);
            clickGemSlot(client);
        }, delay);
    }

    public void clickGemSlot(MinecraftClient client) {
        ScreenHandler screenHandler = getScreenHandler(client);
        if (screenHandler == null) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] clickGemSlot: No screen handler"));
            return;
        }

        String mode = getMode();
        if (mode == null) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] clickGemSlot: No mode set"));
            return;
        }

        long delay = generateDelay();
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Scheduling clickGemSlot (slot 32) in " + delay + "ms for mode: " + mode));
        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Clicking slot 32 for mode: " + mode));
            ClickSlotHandler.getInstance().clickSlotSimulated(client, 32, screenHandler);
            switch (mode) {
                case "order" -> OrderSteps.getInstance().chooseWhichGem(client);
                case "sell" -> SellSteps.getInstance().chooseWhichGem(client);
                default -> MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Unknown mode in clickGemSlot: " + mode));
            }
        }, delay);
    }

    public void clickBestGemSlot(MinecraftClient client) {
        ScreenHandler screenHandler = getScreenHandler(client);
        if (screenHandler == null) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] clickBestGemSlot: No screen handler"));
            return;
        }

        String mode = getMode();
        if (mode == null) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] clickBestGemSlot: No mode set"));
            return;
        }

        long delay = generateDelay();
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Scheduling clickBestGemSlot (slot 15) in " + delay + "ms for mode: " + mode));
        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Clicking slot 15 for mode: " + mode));
            ClickSlotHandler.getInstance().clickSlotSimulated(client, 15, screenHandler);
            switch (mode) {
                case "order" -> OrderSteps.getInstance().createBuyOrder(client);
                case "sell" -> SellSteps.getInstance().createSellOrder(client);
                default -> MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Unknown mode in clickBestGemSlot: " + mode));
            }
        }, delay);
    }

    public void alwaysBeOnTop(MinecraftClient client) {
        ScreenHandler screenHandler = getScreenHandler(client);
        if (screenHandler == null) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] alwaysBeOnTop: No screen handler"));
            return;
        }

        String mode = getMode();
        if (mode == null) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] alwaysBeOnTop: No mode set"));
            return;
        }

        long delay = generateDelay();
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Scheduling alwaysBeOnTop (slot 12) in " + delay + "ms for mode: " + mode));
        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Clicking slot 12 for mode: " + mode));
            ClickSlotHandler.getInstance().clickSlotSimulated(client, 12, screenHandler);
            switch (mode) {
                case "order" -> OrderSteps.getInstance().confirmOrder(client);
                case "sell" -> SellSteps.getInstance().confirmOrder(client);
                default -> MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Unknown mode in alwaysBeOnTop: " + mode));
            }
        }, delay);
    }

    public void manageOrders(MinecraftClient client) {
        ScreenHandler screenHandler = getScreenHandler(client);
        if (screenHandler == null) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] manageOrders: No screen handler"));
            runDelayed(() -> manageOrders(client), generateDelay());
            return;
        }

        String mode = getMode();
        if (mode == null) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] manageOrders: No mode set"));
            return;
        }

        long threeMinutes = 300000;

        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Scheduling manageOrders click (slot 50) in " + threeMinutes + "ms for mode: " + mode));

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Clicking slot 50 in manageOrders for mode: " + mode));
            ClickSlotHandler.getInstance().clickSlotSimulated(client, 50, screenHandler);
            switch (mode) {
                case "order" -> CheckIsFilled.getInstance().checkIfGemIsFilled(client, screenHandler);
                case "sell" -> CheckIsSold.getInstance().checkIfGemIsFilled(client, screenHandler);
                default -> MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Unknown mode in manageOrders: " + mode));
            }
        }, threeMinutes);
    }

    public void performBazaarCommand(MinecraftClient client, long random) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Scheduling Bazaar command in " + random + "ms"));
        if (client.player.networkHandler == null) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] performBazaarCommand: No network handler"));
            return;
        }

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Sending bazaar chat command"));
            client.getNetworkHandler().sendChatCommand("baz");
        }, random);
    }

    public void miningLogic(MinecraftClient client, long random) {
        long delay = random + 500;
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Scheduling miningLogic in " + delay + "ms"));
        runDelayed(() -> {
            ScreenHandler handler = getScreenHandler(client);
            if (handler != null) {
                clickMiningSlot(client, handler);
            } else {
                MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] miningLogic: No screen handler"));
            }
        }, delay);
    }

    public void leftClickPacket(MinecraftClient client) {
        if (client.interactionManager == null || client.targetedEntity == null) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] leftClickPacket: No interaction manager or targeted entity"));
            return;
        }

        Entity entity = client.targetedEntity;
        long delay = generateDelay();
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Scheduling leftClickPacket in " + delay + "ms"));

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Swinging hand and attacking entity"));
            client.player.swingHand(Hand.MAIN_HAND);
            client.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
            client.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(entity, client.player.isSneaking()));

            ScreenHandler handler = getScreenHandler(client);
            if (handler != null) clickMiningSlot(client, handler);
        }, delay);
    }

    public void closeInventory(MinecraftClient client) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Scheduling inventory close in 1000ms"));
        runDelayed(() -> {
            if (client.player != null && client.currentScreen instanceof HandledScreenAccessor) {
                client.player.closeHandledScreen();
                client.setScreen(null);
            }
        }, 1000L);
    }
}
