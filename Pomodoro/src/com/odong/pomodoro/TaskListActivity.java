package com.odong.pomodoro;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by flamen on 14-10-1.
 */
public class TaskListActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_activity);
    }
}