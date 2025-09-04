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
    private static final long random = getInstance().generateDelay();

    public void chooseWhichGem(MinecraftClient client) {
        int slot = getGemSlot(BazaarflipperClient.getInstance().getGem());
        if (slot == -1) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] chooseWhichGem: No gem slot found"));
            return;
        }

        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Choosing gem slot: " + slot));

        long delay = generateDelay();

        clickSlot(client, slot, delay,() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Finished choosing gem, calling clickBestGemSlot"));
            SharedSteps.getInstance().clickBestGemSlot(client);
        });
    }

    public void createSellOrder(MinecraftClient client) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Creating sell order, clicking slot 16"));

        long delay = generateDelay();

        clickSlot(client, 16, delay,() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Finished clicking slot 16, calling bestOfferMinusOne"));
            bestOfferMinusOne(client);
        });
    }

    public void bestOfferMinusOne(MinecraftClient client) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Clicking slot 12 for best offer minus one"));

        long delay = generateDelay();

        clickSlot(client, 12, delay,() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Finished bestOfferMinusOne, calling alwaysBeOnTop"));
            SharedSteps.getInstance().alwaysBeOnTop(client);
        });
    }

    public void confirmOrder(MinecraftClient client) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Confirming sell order, clicking slot 13"));

        long delay = generateDelay();

        clickSlot(client, 13, delay,() -> {
            String message = "<color:#c9ffe2>Je hebt succesvol een sell-offer geplaatst!</color>";
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Sell confirmed: " + message));
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Scheduling inventory close and bazaar command in " + delay + "ms"));
            SharedSteps.getInstance().performBazaarCommand(client, random);
        });

        long delay1 = generateDelay();

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Scheduling manageOrders in " + delay1 + "ms"));
            SharedSteps.getInstance().manageOrders(client);
        }, delay1);
    }
}
