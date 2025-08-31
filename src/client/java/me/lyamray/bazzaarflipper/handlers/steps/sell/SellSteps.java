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

    public void chooseWhichGem(MinecraftClient client) {
        int slot = getGemSlot(BazaarflipperClient.getInstance().getGem());
        if (slot == -1) return;
        clickSlot(client, slot, c -> SharedSteps.getInstance().clickBestGemSlot(c));
    }

    public void createSellOrder(MinecraftClient client) {
        clickSlot(client, 16, this::bestOfferMinusOne);
    }

    public void bestOfferMinusOne(MinecraftClient client) {
        clickSlot(client, 12, c -> SharedSteps.getInstance().alwaysBeOnTop(c));
    }

    public void confirmOrder(MinecraftClient client) {
        clickSlot(client, 13, c -> {
            String message = "<color:#c9ffe2>Je hebt succesvol een sell-offer geplaatst!</color>";
            MessageUtil.sendMessage(MessageUtil.makeComponent(message));
        });
    }
}
