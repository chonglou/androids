package com.odong.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * Created by flamen on 14-9-27.
 */
public class MessageActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

        Intent intent = getIntent();
        setTitle(getString(intent.getIntExtra("title", R.string.app_name)));
        getActionBar().setIcon(intent.getIntExtra("icon", R.drawable.ic_launcher));


        TextView content = (TextView) findViewById(R.id.message_content);
        content.setText(getString(intent.getIntExtra("body", R.string.app_name)));
        content.setMovementMethod(ScrollingMovementMethod.getInstance());


    }
}