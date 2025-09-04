/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.utils.random;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomLong {

    public long generateRandomLong() {
        return 3000L + new Random().nextInt(6000);
    }
}
