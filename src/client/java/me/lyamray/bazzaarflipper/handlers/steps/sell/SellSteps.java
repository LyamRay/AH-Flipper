/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

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
        clickSlot(client, slot, () -> SharedSteps.getInstance().clickBestGemSlot(client));
    }

    public void createSellOrder(MinecraftClient client) {
        clickSlot(client, 16, () -> bestOfferMinusOne(client));
    }

    public void bestOfferMinusOne(MinecraftClient client) {
        clickSlot(client, 12, () -> SharedSteps.getInstance().alwaysBeOnTop(client));
    }

    public void confirmOrder(MinecraftClient client) {
        clickSlot(client, 13, () -> {
            String message = "<color:#c9ffe2>Je hebt succesvol een sell-offer geplaatst!</color>";
            MessageUtil.sendMessage(MessageUtil.makeComponent(message));
        });
    }

}
