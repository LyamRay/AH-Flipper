package me.lyamray.bazzaarflipper.mixin.client;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)

public interface HandledScreenAccessor {
    @Accessor("handler")
    ScreenHandler getHandler();
}