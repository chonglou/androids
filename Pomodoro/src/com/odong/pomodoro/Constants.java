package com.odong.pomodoro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by flamen on 14-9-30.
 */
public class Constants {
    private Constants() {
    }

    public static void alert(Context context, String message) {

        new AlertDialog.Builder(context).setTitle(R.string.dlg_error_title)
                .setMessage(message)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    public final static String STORAGE_SETTINGS_NAME = "settings";
    public final static String KEY_TASK_COUNTER = "task.counter";
    public final static String KEY_TASK_TIMER = "task.timer";
    public final static String KEY_TASK_SHORT_BREAK = "task.short_break";
    public final static String KEY_TASK_LONGER_BREAK = "task.longer_break";

    public final static int[] ITEMS_TASK_COUNTER = {3, 4, 5};
    public final static int[] ITEMS_TASK_TIMER = {20, 25, 30, 45, 60};
    public final static int[] ITEMS_TASK_SHORT_BREAK = {2, 3, 5, 10};
    public final static int[] ITEMS_TASK_LONGER_BREAK = {10, 15, 20, 30, 45};
}
