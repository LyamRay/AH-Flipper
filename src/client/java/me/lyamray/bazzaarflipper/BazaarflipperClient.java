/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper;

import lombok.Getter;
import lombok.Setter;
import me.lyamray.bazzaarflipper.handlers.command.CommandHandler;
import me.lyamray.bazzaarflipper.utils.messages.Gems;
import me.lyamray.bazzaarflipper.utils.messages.MessageUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@Setter
public class BazaarflipperClient implements ClientModInitializer {

    @Getter
    private static BazaarflipperClient instance;
    private MinecraftClient client;
    private String gem = null;
    private String sellOrOrder = null;

    public void onInitializeClient() {
        instance = this;
        startClientTick();
        endClientTick();
        clientSendMessageEvents();
    }

    private void startClientTick() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.world == null && client.player == null) return;
            this.client = client;
        });
    }

    private void endClientTick() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null && client.player == null) return;
            this.client = client;
        });
    }

    private void clientSendMessageEvents() {
        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            String trimmed = message.trim();
            String[] parts = trimmed.split("\\s+");

            if (!trimmed.startsWith("*bf")) {
                return true;
            }

            if (parts.length < 2) {
                String allGems = Arrays.stream(Gems.values())
                        .map(g -> g.name().toLowerCase())
                        .collect(Collectors.joining(", "));

                String failureMessage = "<color:#b2ac9f>Je moet 1 van de gems kiezen: {allgems}!</color>"
                        .replace("{allgems}", allGems);

                MessageUtil.sendMessage(MessageUtil.makeComponent(failureMessage));
                return false;
            }

            if (parts[1].equalsIgnoreCase("stop")) {
                CommandHandler.getInstance().stopFlipping();
                return false;
            }

            try {
                Gems g = Gems.valueOf(parts[1].toUpperCase());
                gem = g.name().toLowerCase();

                CommandHandler.getInstance().startFlipping(gem);
                return false;
            } catch (IllegalArgumentException e) {
                MessageUtil.sendMessage(MessageUtil.makeComponent(" <color:#b2ac9f>Deze gem bestaat niet!</color>"));
            }
            return false;
        });
    }
}
