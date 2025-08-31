package me.lyamray.bazzaarflipper.handlers.steps.handler;

import net.minecraft.client.MinecraftClient;

@FunctionalInterface
public interface FlipperAction {
    void execute(MinecraftClient client);
}