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

    private static final long random = getInstance().generateDelay();

    public void chooseWhichGem(MinecraftClient client) {
        int slot = getGemSlot(BazaarflipperClient.getInstance().getGem());
        if (slot == -1) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][OrderSteps] chooseWhichGem: No gem slot found"));
            return;
        }

        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][OrderSteps] Choosing gem slot: " + slot));
        long delay = generateDelay();
        clickSlot(client, slot, delay,() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][OrderSteps] Finished choosing gem, calling clickBestGemSlot"));
            SharedSteps.getInstance().clickBestGemSlot(client);
        });
    }

    public void createBuyOrder(MinecraftClient client) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][OrderSteps] Creating buy order, clicking slot 15"));
        long delay = generateDelay();
        clickSlot(client, 15, delay,() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][OrderSteps] Finished clicking slot 15, calling buyOneGem"));
            buyOneGem(client);
        });
    }

    public void buyOneGem(MinecraftClient client) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][OrderSteps] Clicking slot 10 to buy one gem"));
        long delay = generateDelay();
        clickSlot(client, 10, delay,() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][OrderSteps] Finished buying one gem, calling alwaysBeOnTop"));
            SharedSteps.getInstance().alwaysBeOnTop(client);
        });
    }

    public void confirmOrder(MinecraftClient client) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][OrderSteps] Confirming order, clicking slot 13"));

        long delay = generateDelay();

        clickSlot(client, 13, delay,() -> {
            String message = "<color:#c9ffe2>Je hebt succesvol een buy-order geplaatst!</color>";
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][OrderSteps] Order confirmed: " + message));
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][OrderSteps] Scheduling inventory close and bazaar command in " + delay + "ms"));
            SharedSteps.getInstance().performBazaarCommand(client, random);
        });

        long delay1 = delay + generateDelay();

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][OrderSteps] Scheduling manageOrders in " + delay1 + "ms"));
            SharedSteps.getInstance().manageOrders(client);
        }, delay1);
    }
}
