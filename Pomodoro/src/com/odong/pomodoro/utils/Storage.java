package com.odong.pomodoro.utils;

import android.content.Context;

/**
 * Created by flamen on 14-9-30.
 */
public class Storage {
    public static synchronized Storage getInstance() {
        return instance;
    }

    public static synchronized void set(Context context) {
        instance = new Storage(context);
    }

    private static Storage instance;

    private Storage(Context context) {
        this.context = context;
    }


    private final Context context;


}
