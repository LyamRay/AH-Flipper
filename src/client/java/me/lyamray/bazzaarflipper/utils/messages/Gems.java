/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */
package me.lyamray.bazzaarflipper.utils.messages;

import lombok.Getter;

@Getter
public enum Gems {

    JADE(10),
    AMBER(11),
    TOPAZ(12),
    SAPPHIRE(13),
    AMETHYST(14),
    JASPER(15),
    RUBY(16),
    OPAL(20),
    ONYX(21),
    AQUAMARINE(22),
    CITRINE(23),
    PERIDOT(24);

    final int slot;
    Gems(int slot) { this.slot = slot; }
}
