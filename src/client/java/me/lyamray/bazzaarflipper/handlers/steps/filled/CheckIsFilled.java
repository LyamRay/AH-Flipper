package me.lyamray.bazzaarflipper.handlers.steps.filled;

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

public class CheckIsFilled extends BaseSteps {

    @Getter
    private static final CheckIsFilled instance = new CheckIsFilled();

    private static final int CHECK_SLOT = 19;

    public void checkIfGemIsFilled(MinecraftClient client, ScreenHandler handler) {
        if (!(client.currentScreen instanceof HandledScreen<?>)) return;

        Slot slot = getSlot(handler, CHECK_SLOT);
        if (slot == null || !slot.hasStack()) return;

        ItemStack stack = slot.getStack();
        int tooltipLines = getTooltipLines(stack, client);

        if (tooltipLines > 10) {
            runDelayed(client, generateDelay(), c -> {
                ClickSlotHandler.getInstance().clickSlotSimulated(c, CHECK_SLOT, handler);
                SharedSteps.getInstance().closeInventory(c);
                BazaarflipperClient.getInstance().setSellOrOrder("sell");
            });
        } else {
            runDelayed(client, generateDelay(), c -> {
                ClickSlotHandler.getInstance().clickSlotSimulated(c, CHECK_SLOT, handler);
                // Next action can be buying logic
            });
        }
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
