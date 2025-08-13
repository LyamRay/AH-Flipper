package me.lyamray.bazzaarflipper.utils;

import lombok.Getter;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {

    @Getter
    private static TimerUtil instance = new TimerUtil();

    private final Timer timer = new Timer();

    public Timer runTaskLater(long delay) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, delay);
        return timer;
    }

    public Timer runTaskTimer(long delay, long period) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Your repeating task logic here
            }
        }, delay, period);
        return timer;
    }
}
