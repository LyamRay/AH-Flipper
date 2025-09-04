package me.lyamray.bazzaarflipper.handlers.steps.filled;

import lombok.Getter;
import me.lyamray.bazzaarflipper.BazaarflipperClient;
import me.lyamray.bazzaarflipper.handlers.slot.ClickSlotHandler;
import me.lyamray.bazzaarflipper.handlers.steps.handler.BaseSteps;
import me.lyamray.bazzaarflipper.handlers.steps.shared.SharedSteps;
import me.lyamray.bazzaarflipper.utils.messages.MessageUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import java.util.List;

public class CheckIsFilled extends BaseSteps {

    @Getter
    private static final CheckIsFilled instance = new CheckIsFilled();

    private static final int CHECK_SLOT = 19;

    public void checkIfGemIsFilled(MinecraftClient client, ScreenHandler handler) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] checkIfGemIsFilled called"));

        if (!(client.currentScreen instanceof HandledScreen<?>)) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Current screen is not a HandledScreen"));
            return;
        }

        Slot slot = getSlot(handler, CHECK_SLOT);
        if (slot == null) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Slot " + CHECK_SLOT + " is null"));
            return;
        }

        if (!slot.hasStack()) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Slot " + CHECK_SLOT + " has no item stack"));
            return;
        }

        ItemStack stack = slot.getStack();
        int tooltipLines = getTooltipLines(stack, client);

        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Slot " + CHECK_SLOT + " tooltip lines: " + tooltipLines));

        if (tooltipLines >= 10) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Buy-order is filled. Executing sell logic."));
            BazaarflipperClient.getInstance().setSellOrOrder("sell");
            doSellLogic(client, handler);
        } else {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Buy-order is not filled. Executing buy logic."));
            BazaarflipperClient.getInstance().setSellOrOrder("order");
            redoBuyLogic(client, handler);
        }
    }

    private void doSellLogic(MinecraftClient client, ScreenHandler handler) {
        long firstDelay = generateDelay();
        long secondDelay = firstDelay + generateDelay();

        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] doSellLogic: first click in " + firstDelay + "ms, second in " + secondDelay + "ms"));

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Clicking slot " + CHECK_SLOT + " for sell"));
            ClickSlotHandler.getInstance().clickSlotSimulated(client, CHECK_SLOT, handler);
        }, firstDelay);

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Closing inventory and performing Bazaar command (sell)"));
            SharedSteps.getInstance().closeInventory(client);

            long delay = generateDelay();
            SharedSteps.getInstance().performBazaarCommand(client, delay);

            long delay1 = delay + generateDelay();
            SharedSteps.getInstance().miningLogic(client, delay1);
        }, secondDelay);
    }

    private void redoBuyLogic(MinecraftClient client, ScreenHandler handler) {
        long firstDelay = generateDelay();
        long secondDelay = firstDelay + generateDelay();
        long thirdDelay = secondDelay + generateDelay();

        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] redoBuyLogic: delays first=" + firstDelay + ", second=" + secondDelay + ", third=" + thirdDelay));

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Clicking slot " + CHECK_SLOT + " for buy logic"));
            ClickSlotHandler.getInstance().clickSlotSimulated(client, CHECK_SLOT, handler);
        }, firstDelay);

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Clicking slot 11 for buy logic"));
            ClickSlotHandler.getInstance().clickSlotSimulated(client, 11, handler);
        }, secondDelay);

        runDelayed(() -> {

            long delay = generateDelay();
            long delay2 = delay + generateDelay();

            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Closing inventory and performing Bazaar command (buy)"));
            SharedSteps.getInstance().closeInventory(client);
            SharedSteps.getInstance().performBazaarCommand(client, delay2);
            SharedSteps.getInstance().miningLogic(client, delay2);
        }, thirdDelay);
    }

    private int getTooltipLines(ItemStack stack, MinecraftClient client) {
        List<Text> tooltip = stack.getTooltip(
                net.minecraft.item.Item.TooltipContext.DEFAULT,
                client.player,
                TooltipType.BASIC
        );
        return tooltip.size();
    }
}
