package com.odong.pomodoro;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import com.odong.pomodoro.store.Storage;

import java.util.Calendar;

/**
 * Created by flamen on 14-10-1.
 */
public class TaskAddActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add_activity);

        initEvents();
    }
    private void initEvents(){
        findViewById(R.id.tv_task_add_todo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(TaskAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        TextView tv = (TextView) findViewById(R.id.tv_task_add_todo);
                        String text = String.format(getResources().getString(R.string.lbl_date), year, monthOfYear+1, dayOfMonth);
                        tv.setText(Html.fromHtml(text));
                    }
                }, year, month,day).show();
            }
        });
        findViewById(R.id.btn_task_add_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((EditText)findViewById(R.id.tv_task_add_title)).getText().toString().trim();
                String todo = ((EditText)findViewById(R.id.tv_task_add_todo)).getText().toString().trim();
                if(title.isEmpty() || todo.isEmpty()){
                    Constants.alert(TaskAddActivity.this, getString(R.string.lbl_need_input));
                }
                else {
                    Storage.getInstance().addTask(title, ((EditText)findViewById(R.id.tv_task_add_content)).getText().toString().trim(),todo);
                    finish();
                }

            }
        });
        findViewById(R.id.btn_task_add_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}