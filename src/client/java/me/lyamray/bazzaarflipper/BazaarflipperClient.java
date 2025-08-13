package me.lyamray.bazzaarflipper;

import lombok.Getter;
import lombok.Setter;
import me.lyamray.bazzaarflipper.utils.Gems;
import me.lyamray.bazzaarflipper.utils.MessageUtil;
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
    private String gem;

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

            if (!trimmed.startsWith("*sb")) {
                return true;
            }

            if (parts.length < 2) {
                String allGems = Arrays.stream(Gems.values())
                        .map(g -> g.name().toLowerCase())
                        .collect(Collectors.joining(", "));

                String failureMessage = "<color:#c9ffe2>Je moet 1 van de gems kiezen: {allgems}!</color>"
                        .replace("{allgems}", allGems);

                MessageUtil.sendMessage(MessageUtil.deserialize(failureMessage));
                return false;
            }

            try {
                Gems g = Gems.valueOf(parts[1].toUpperCase());
                gem = g.name().toLowerCase();
                int slot = g.getSlot();

                String succesMessage = "<color:#c9ffe2>Je bent succesvol gestart met het flippen van de gem: {gemname}!</color)"
                        .replace("{gemname}", gem);
                MessageUtil.sendMessage(MessageUtil.deserialize(succesMessage));

            } catch (IllegalArgumentException e) {
                assert client.player != null;
                MessageUtil.sendMessage(MessageUtil.deserialize(" <color:#b2ac9f>Deze gem bestaat niet!</color>"));
            }

            return false;
        });
    }

}
