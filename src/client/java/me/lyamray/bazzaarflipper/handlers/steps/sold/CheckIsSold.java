package me.lyamray.bazzaarflipper.handlers.steps.sold;

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

public class CheckIsSold extends BaseSteps {

    @Getter
    private static final CheckIsSold instance = new CheckIsSold();

    private static final int CHECK_SLOT = 10;

    public void checkIfGemIsFilled(MinecraftClient client, ScreenHandler handler) {
        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] checkIfGemIsFilled (sold) called"));

        if (!(client.currentScreen instanceof HandledScreen<?>)) return;

        Slot slot = getSlot(handler, CHECK_SLOT);
        if (slot == null) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Slot " + CHECK_SLOT + " is null"));
            return;
        }

        if (!slot.hasStack()) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Slot " + CHECK_SLOT + " has no stack"));
            return;
        }

        ItemStack stack = slot.getStack();
        int tooltipLines = getTooltipLines(stack, client);

        MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Slot " + CHECK_SLOT + " tooltip lines: " + tooltipLines));

        if (tooltipLines >= 10) {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Gem is sold. Executing buy logic"));
            BazaarflipperClient.getInstance().setSellOrOrder("order");
            gemSold(client, handler);
        } else {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Gem is not sold. Executing sell logic"));
            BazaarflipperClient.getInstance().setSellOrOrder("sell");
            redoSellLogic(client, handler);
        }
    }

    private void gemSold(MinecraftClient client, ScreenHandler handler) {
        long firstDelay = generateDelay();
        long secondDelay = firstDelay + generateDelay();

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Clicking slot " + CHECK_SLOT + " (sold)"));
            ClickSlotHandler.getInstance().clickSlotSimulated(client, CHECK_SLOT, handler);

            String message = "<color:#c9ffe2>Je hebt succesvol een gem gesold!</color>";
            MessageUtil.sendMessage(MessageUtil.makeComponent(message));
        }, firstDelay);

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Closing inventory and performing Bazaar command (sold)"));
            SharedSteps.getInstance().closeInventory(client);

            long delay = generateDelay();
            SharedSteps.getInstance().performBazaarCommand(client, delay);

            long delay1 = delay + generateDelay();
            SharedSteps.getInstance().miningLogic(client, delay1);
        }, secondDelay);
    }

    private void redoSellLogic(MinecraftClient client, ScreenHandler handler) {
        long firstDelay = generateDelay();
        long secondDelay = firstDelay + generateDelay();
        long thirdDelay = secondDelay + generateDelay();

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Clicking slot " + CHECK_SLOT + " (redo sell)"));
            ClickSlotHandler.getInstance().clickSlotSimulated(client, CHECK_SLOT, handler);
        }, firstDelay);

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Clicking slot 11 (redo sell)"));
            ClickSlotHandler.getInstance().clickSlotSimulated(client, 13, handler);
            SharedSteps.getInstance().closeInventory(client);
        }, secondDelay);

        runDelayed(() -> {
            MessageUtil.sendMessage(MessageUtil.makeComponent("[DEBUG] Closing inventory, performing Bazaar command and mining logic"));
            SharedSteps.getInstance().performBazaarCommand(client, generateDelay());
            SharedSteps.getInstance().miningLogic(client, generateDelay());
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
