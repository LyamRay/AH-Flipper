package me.lyamray.bazzaarflipper.handlers.steps.order;

import lombok.Getter;
import me.lyamray.bazzaarflipper.BazaarflipperClient;
import me.lyamray.bazzaarflipper.handlers.steps.shared.SharedSteps;
import me.lyamray.bazzaarflipper.utils.messages.MessageUtil;
import net.minecraft.client.MinecraftClient;

public class OrderSteps extends me.lyamray.bazzaarflipper.handlers.steps.handler.AbstractGemStep {

    @Getter
    private static final OrderSteps instance = new OrderSteps();

    public void chooseWhichGem(MinecraftClient client) {
        int slot = getGemSlot(BazaarflipperClient.getInstance().getGem());
        if (slot == -1) return;
        clickSlot(client, slot, c -> SharedSteps.getInstance().clickBestGemSlot(c));
    }

    public void createBuyOrder(MinecraftClient client) {
        clickSlot(client, 15, this::buyOneGem);
    }

    public void buyOneGem(MinecraftClient client) {
        clickSlot(client, 10, c -> SharedSteps.getInstance().alwaysBeOnTop(c));
    }

    public void confirmOrder(MinecraftClient client) {
        clickSlot(client, 13, c -> {
            String message = "<color:#c9ffe2>Je hebt succesvol een buy-order geplaatst!</color>";
            MessageUtil.sendMessage(MessageUtil.makeComponent(message));
            SharedSteps.getInstance().manageOrders(c);
        });
    }
}
