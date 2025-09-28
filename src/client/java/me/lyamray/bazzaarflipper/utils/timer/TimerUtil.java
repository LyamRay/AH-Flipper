/*
 * DISCLAIMER:
 * This mod was intentionally created for a private project of the creator of this mod.
 * Use of this mod on any other server is at your own risk.
 * The creator of this mod is not responsible for any actions, damages, or consequences
 * that may occur from its use outside the intended private project.
 */

package me.lyamray.bazzaarflipper.utils.timer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TimerUtil {

    @Getter
    private static final TimerUtil instance = new TimerUtil();

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void runTaskLater(Runnable task, long delay) {
        scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
    }


    public void cancelAll() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            scheduler = Executors.newScheduledThreadPool(1);
        }
    }
}
