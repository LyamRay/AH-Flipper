/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

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
        if (slot == -1) return;

        clickSlot(client, slot, () -> SharedSteps.getInstance().clickBestGemSlot(client));
    }

    public void createBuyOrder(MinecraftClient client) {
        clickSlot(client, 15, () -> buyOneGem(client));
    }

    public void buyOneGem(MinecraftClient client) {
        clickSlot(client, 10, () -> SharedSteps.getInstance().alwaysBeOnTop(client));
    }

    public void confirmOrder(MinecraftClient client) {
        clickSlot(client, 13, () -> {
            String message = "<color:#c9ffe2>Je hebt succesvol een buy-order geplaatst!</color>";
            MessageUtil.sendMessage(MessageUtil.makeComponent(message));
            SharedSteps.getInstance().manageOrders(client);
        });

        runDelayed(() -> {
            SharedSteps.getInstance().closeInventory(client);
            SharedSteps.getInstance().performBazaarCommand(client, random);
        }, generateDelay());
    }
}
