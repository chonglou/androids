package com.odong.pomodoro;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.odong.pomodoro.utils.TaskQueue;

/**
 * Created by flamen on 14-9-30.
 */
public class SettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        initSpinner(R.id.sp_settings_tasks,
                R.array.sp_itemTexts_settings_tasks,
                R.array.sp_itemValues_settings_tasks,
                Constants.KEY_TASK_COUNTER,
                R.integer.sp_default_settings_timer
                );

        initSpinner(R.id.sp_settings_timer,
                R.array.sp_itemTexts_settings_timer,
                R.array.sp_itemValues_settings_timer,
                Constants.KEY_TASK_TIMER,
                R.integer.sp_default_settings_timer);

        initSpinner(R.id.sp_settings_short_break,
                R.array.sp_itemTexts_settings_short_break,
                R.array.sp_itemValues_settings_short_break,
                Constants.KEY_TASK_SHORT_BREAK,
                R.integer.sp_default_settings_short_break);

        initSpinner(R.id.sp_settings_longer_break,
                R.array.sp_itemTexts_settings_longer_break,
                R.array.sp_itemValues_settings_longer_break,
                Constants.KEY_TASK_LONGER_BREAK,
                R.integer.sp_default_settings_longer_break);
    }


    private void initSpinner(int id, int items, final int values, final String key, int def) {
        Spinner spinner = (Spinner) findViewById(id);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int val = getSharedPreferences(Constants.STORAGE_SETTINGS_NAME, 0).getInt(key, getResources().getInteger(def));
        int[] vals = getResources().getIntArray(values);
        for(int i=0; i<vals.length; i++){
            if(vals[i] == val){
                spinner.setSelection(i);
            }
        }



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences.Editor editor = getSharedPreferences(Constants.STORAGE_SETTINGS_NAME, 0).edit();
                editor.putInt(key, getResources().getIntArray(values)[position]);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}