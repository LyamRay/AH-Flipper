package me.lyamray.bazzaarflipper;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;

public class BazaarflipperClient implements ClientModInitializer {

    @Getter
    private static BazaarflipperClient instance;
    private MinecraftClient client;

    @Override
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
            //End Client Tick Events (prob. not using)
            this.client = client;
        });
    }

    private void clientSendMessageEvents() {
        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            String trimmed = message.trim();
            String[] parts = trimmed.split("\\s+");
            if (parts.length == 0) return true;

            if (!trimmed.startsWith("*")) return true;

//            return switch (parts[0].toLowerCase()) {
//                case "*harvex" -> {
//                    //handleAutoPlukCommand();
//
//                }
//                case "*npcskan" -> {
//                    //NpcScanHandler.getInstance().handleNPCScanCommand(parts);
//
//                }
//                case "*inskan" -> {
//                    //InteractionEntityScanHandler.getInstance().handleInteractionScanCommand(parts);
//
//                }
//
//                default -> {
//                    //handleSafetyCommand();
//                    yield false;
//                }
//            };
            return false;
        });
    }
}
