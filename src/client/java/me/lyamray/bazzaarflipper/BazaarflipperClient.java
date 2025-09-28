/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper;

import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.Setter;
import me.lyamray.bazzaarflipper.handlers.command.CommandHandler;
import me.lyamray.bazzaarflipper.utils.messages.Gems;
import me.lyamray.bazzaarflipper.utils.messages.MessageUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@Setter
public class BazaarflipperClient implements ClientModInitializer {

    @Getter
    private static BazaarflipperClient instance;
    private MinecraftClient client;
    private String gem = null;
    private boolean filled = false;
    private boolean sold = false;

    @Override
    public void onInitializeClient() {
        instance = this;
        startClientTick();
        endClientTick();
        clientSendMessageEvents();
        clientReceiveChatMessages();
    }

    private void startClientTick() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.world == null || client.player == null) return;
            this.client = client;
        });
    }

    private void endClientTick() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null || client.player == null) return;
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

    private void clientReceiveChatMessages() {
        ClientReceiveMessageEvents.CHAT.register(this::handleChatMessage);
        ClientReceiveMessageEvents.GAME.register(this::handleSystemMessage);
    }

    private void handleSystemMessage(Text message, boolean overlay) {
        if (message == null) return;

        String rawMsg = message.getString();
        String msg = rawMsg.replaceAll("§[0-9A-FK-ORa-fk-or]", "");

        System.out.println("SYSTEM RAW: [" + rawMsg + "]");
        System.out.println("SYSTEM MSG: [" + msg + "]");

        if (msg.contains("[Bazaar]") && msg.contains("Buy Order") && msg.contains("was filled")) {
            filled = true;
            MessageUtil.sendMessage(MessageUtil.makeComponent("[GG] Je buy-order is gefilled!"));
        } else if (msg.contains("[Bazaar]") && msg.contains("Sell Offer") && msg.contains("was filled")) {
            sold = true;
            MessageUtil.sendMessage(MessageUtil.makeComponent("[GG] Je sell-offer is gesold!"));
        }
    }

    private void handleChatMessage(Text message, SignedMessage signedMessage,
                                   GameProfile sender, MessageType.Parameters params,
                                   Instant receptionTimestamp) {
        if (message == null) return;

        String rawMsg = message.getString();
        String msg = rawMsg.replaceAll("§[0-9A-FK-ORa-fk-or]", "");

        MessageUtil.sendMessage(MessageUtil.makeComponent(msg));

        System.out.println("CHAT RAW: [" + rawMsg + "]");
        System.out.println("CHAT MSG: [" + msg + "]");

        if (msg.contains("[Bazaar]") && msg.contains("Buy Order") && msg.contains("was filled")) {
            filled = true;
            MessageUtil.sendMessage(MessageUtil.makeComponent("[GG] Je buy-order is gefilled!"));
        } else if (msg.contains("[Bazaar]") && msg.contains("Sell Offer") && msg.contains("was sold")) {
            sold = true;
            MessageUtil.sendMessage(MessageUtil.makeComponent("[GG] Je sell-offer is gesold!"));
        }
    }
}
