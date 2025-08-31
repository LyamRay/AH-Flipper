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

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class TimerUtil {

    @Getter
    private static final TimerUtil instance = new TimerUtil();

    private final Timer timer = new Timer(true);

    public void runTaskLater(Runnable task, long delay) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    log.error("Error in scheduled task", e);
                }
            }
        }, delay);
    }

    public void runTaskTimer(Runnable task, long delay, long period) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    log.error("Error in repeating task", e);
                }
            }
        }, delay, period);
    }

    public void cancelAll() {
        timer.cancel();
        log.info("All scheduled tasks canceled");
    }
}