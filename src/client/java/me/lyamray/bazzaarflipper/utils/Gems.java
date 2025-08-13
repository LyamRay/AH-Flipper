package me.lyamray.bazzaarflipper.utils;

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
