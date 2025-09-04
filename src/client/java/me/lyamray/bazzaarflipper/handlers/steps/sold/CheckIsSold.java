/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.handlers.steps.sold;

import lombok.Getter;
import me.lyamray.bazzaarflipper.BazaarflipperClient;
import me.lyamray.bazzaarflipper.handlers.slot.ClickSlotHandler;
import me.lyamray.bazzaarflipper.handlers.steps.handler.BaseSteps;
import me.lyamray.bazzaarflipper.handlers.steps.shared.SharedSteps;
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
    private static CheckIsSold instance = new CheckIsSold();

    private static final int CHECK_SLOT = 10;
    private static final long random = getInstance().generateDelay();

    public void checkIfGemIsFilled(MinecraftClient client, ScreenHandler handler) {
        if (!(client.currentScreen instanceof HandledScreen<?>)) return;

        Slot slot = getSlot(handler, CHECK_SLOT);
        if (slot == null || !slot.hasStack()) return;

        ItemStack stack = slot.getStack();
        int tooltipLines = getTooltipLines(stack, client);

        if (tooltipLines > 10) { //if sold
            BazaarflipperClient.getInstance().setSellOrOrder("buy");
            gemSold(client, handler);
        } else { //if not sold
            BazaarflipperClient.getInstance().setSellOrOrder("sell");
            redoSellLogic(client, handler);
        }
    }

    private void gemSold(MinecraftClient client, ScreenHandler handler) {
        long firstDelay = generateDelay();
        long secondDelay = firstDelay + generateDelay();

        runDelayed(() -> ClickSlotHandler.getInstance().clickSlotSimulated(client, CHECK_SLOT, handler), firstDelay);

        runDelayed(() -> {
            SharedSteps.getInstance().closeInventory(client);
            SharedSteps.getInstance().performBazaarCommand(client, random);
        }, secondDelay);
    }

    private void redoSellLogic(MinecraftClient client, ScreenHandler handler) {
        long firstDelay = generateDelay();
        long secondDelay = firstDelay + generateDelay();
        long thirdDelay = secondDelay + generateDelay();

        runDelayed(() -> ClickSlotHandler.getInstance().clickSlotSimulated(client, CHECK_SLOT, handler), firstDelay);

        runDelayed(() -> ClickSlotHandler.getInstance().clickSlotSimulated(client, 11, handler), secondDelay);

        runDelayed(() -> {
            SharedSteps.getInstance().closeInventory(client);
            SharedSteps.getInstance().performBazaarCommand(client, random);
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

