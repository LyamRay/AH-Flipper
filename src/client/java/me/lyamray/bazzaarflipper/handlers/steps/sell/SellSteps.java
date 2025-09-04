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
        clickSlot(client, slot, () -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Finished choosing gem, calling clickBestGemSlot"));
            SharedSteps.getInstance().clickBestGemSlot(client);
        });
    }

    public void createSellOrder(MinecraftClient client) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Creating sell order, clicking slot 16"));
        clickSlot(client, 16, () -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Finished clicking slot 16, calling bestOfferMinusOne"));
            bestOfferMinusOne(client);
        });
    }

    public void bestOfferMinusOne(MinecraftClient client) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Clicking slot 12 for best offer minus one"));
        clickSlot(client, 12, () -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Finished bestOfferMinusOne, calling alwaysBeOnTop"));
            SharedSteps.getInstance().alwaysBeOnTop(client);
        });
    }

    public void confirmOrder(MinecraftClient client) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Confirming sell order, clicking slot 13"));
        clickSlot(client, 13, () -> {
            String message = "<color:#c9ffe2>Je hebt succesvol een sell-offer geplaatst!</color>";

            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][SellSteps] Sell confirmed: " + message));
            SharedSteps.getInstance().manageOrders(client);

            long delay = generateDelay();
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG][OrderSteps] Scheduling inventory close and bazaar command in " + delay + "ms"));

            runDelayed(() -> {
                SharedSteps.getInstance().closeInventory(client);
                SharedSteps.getInstance().performBazaarCommand(client, random);
            }, delay);
        });
    }
}
