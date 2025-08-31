/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.utils.messages;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public enum Messages {

    GEM_DOES_NOT_EXIST(() -> ""),
    SUCCESSFULLY_STARTED_FLIPPING_GEM(() -> ""),
    CHOSE_ONE_GEM(() -> "");


    private final Supplier<String> messageSupplier;

    Messages(Supplier<String> messageSupplier) {
        this.messageSupplier = messageSupplier;
    }

    public String getMessage() {
        return messageSupplier.get();
    }
}
