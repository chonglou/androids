package com.odong.pomodoro;

/**
 * Created by flamen on 14-9-30.
 */
public class Constants {
    private Constants() {
    }

    public final static String KEY_TASK_COUNTER = "task.counter";
    public final static String KEY_TASK_TIMER = "task.timer";
    public final static String KEY_TASK_SHORT_BREAK = "task.short_break";
    public final static String KEY_TASK_LONGER_BREAK = "task.longer_break";

    public final static int[] ITEMS_TASK_COUNTER = {3,4,5};
    public final static int[] ITEMS_TASK_TIMER = {20*60, 25*60, 30*60, 45*60, 60*60};
    public final static int[] ITEMS_TASK_SHORT_BREAK = {2*60,3*60,5*60,10*60};
    public final static int[] ITEMS_TASK_LONGER_BREAK = {10*60, 15*60, 20*60, 30*60, 45*60};
}
