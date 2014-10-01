package com.odong.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.odong.rssreader.store.Storage;

import java.util.Arrays;

/**
 * Created by flamen on 14-9-27.
 */
public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        initSpinner(R.id.sp_settings_keep,
                R.array.sp_itemTexts_settings_keep,
                R.array.sp_itemValues_settings_keep,
                "feed.keep.index",
                R.integer.sp_default_settings_keep);
        initSpinner(R.id.sp_settings_refresh,
                R.array.sp_itemTexts_settings_refresh,
                R.array.sp_itemValues_settings_refresh,
                "refresh.space.index",
                R.integer.sp_default_settings_refresh);

        initNotice();

    }


    private void initSpinner(int id, int texts, final int values, final String key, int defVal) {
        Spinner keep = (Spinner) findViewById(id);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, texts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        keep.setAdapter(adapter);

        String sel = Storage.getInstance().get(key);
        int val = sel == null ? getResources().getInteger(defVal) : Integer.parseInt(sel);

        int i=0;
        int[] vs = getResources().getIntArray(values);
        for(;i<vs.length;i++){
            if(vs[i] == val){
                keep.setSelection(i);
                break;
            }
        }

        keep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String val = Integer.toString(getResources().getIntArray(values)[position]);
                Storage.getInstance().set(key, val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initNotice() {
        Switch notice = (Switch) findViewById(R.id.sw_setting_notice);

        String val = Storage.getInstance().get("notice.enable");
        notice.setChecked(val == null || Boolean.parseBoolean(val));

        notice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Storage.getInstance().set("notice.enable", Boolean.toString(isChecked));
            }
        });
    }


}