package me.lyamray.bazzaarflipper.handlers.command;

import lombok.Getter;
import me.lyamray.bazzaarflipper.BazaarflipperClient;
import me.lyamray.bazzaarflipper.handlers.steps.shared.SharedSteps;
import me.lyamray.bazzaarflipper.utils.messages.MessageUtil;
import me.lyamray.bazzaarflipper.utils.random.RandomLong;
import me.lyamray.bazzaarflipper.utils.timer.TimerUtil;
import net.minecraft.client.MinecraftClient;

public class CommandHandler {

    @Getter
    private static final CommandHandler instance = new CommandHandler();

    public void startFlipping(String gem) {
        MinecraftClient client = BazaarflipperClient.getInstance().getClient();
        if (client == null) return;

        BazaarflipperClient.getInstance().setGem(gem);
        BazaarflipperClient.getInstance().setSellOrOrder("order");

        long delay = RandomLong.generateRandomLong();

        TimerUtil.getInstance().runTaskLater(() ->
                SharedSteps.getInstance().performBazaarCommand(client), delay
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
