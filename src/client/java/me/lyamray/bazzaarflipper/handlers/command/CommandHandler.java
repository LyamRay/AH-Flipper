/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.handlers.command;

import lombok.Getter;
import me.lyamray.bazzaarflipper.BazaarflipperClient;
import me.lyamray.bazzaarflipper.handlers.steps.handler.BaseSteps;
import me.lyamray.bazzaarflipper.handlers.steps.shared.SharedSteps;
import me.lyamray.bazzaarflipper.utils.messages.MessageUtil;
import me.lyamray.bazzaarflipper.utils.random.RandomLong;
import me.lyamray.bazzaarflipper.utils.timer.TimerUtil;
import net.minecraft.client.MinecraftClient;

public class CommandHandler extends BaseSteps {

    @Getter
    private static final CommandHandler instance = new CommandHandler();

    public void startFlipping(String gem) {
        MinecraftClient client = BazaarflipperClient.getInstance().getClient();
        if (client == null) return;

        BazaarflipperClient.getInstance().setGem(gem);
        BazaarflipperClient.getInstance().setSellOrOrder("order");

        long delay = RandomLong.generateRandomLong();
        long random = generateDelay();

        TimerUtil.getInstance().runTaskLater(() -> {
                    SharedSteps.getInstance().performBazaarCommand(client, random);
                    SharedSteps.getInstance().miningLogic(client, random + 500);
                }, delay
        );

        String successMessage = "<color:#c9ffe2>Je bent succesvol gestart met het flippen van de gem: {gemname}!</color>"
                .replace("{gemname}", gem);
        MessageUtil.sendMessage(MessageUtil.makeComponent(successMessage));
    }

    public void stopFlipping() {
        String currentGem = BazaarflipperClient.getInstance().getGem();
        if (currentGem == null) return;

        String stopMessage = "<color:#b2ac9f>Je bent gestopt met het flippen van de gem: {gemname}!</color>"
                .replace("{gemname}", currentGem);
        MessageUtil.sendMessage(MessageUtil.makeComponent(stopMessage));

        BazaarflipperClient.getInstance().setGem(null);
        BazaarflipperClient.getInstance().setSellOrOrder(null);
    }
}
