package me.lyamray.bazzaarflipper.mixin.client;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenClose {
    @Inject(method = "removed", at = @At("TAIL"))
    private void onGuiRemoved(CallbackInfo ci) {
        //CheckGUIHandler.getInstance().onGuiClosed();
    }
}
