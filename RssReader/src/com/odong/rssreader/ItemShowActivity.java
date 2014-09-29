package com.odong.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by flamen on 14-9-22.
 */
public class ItemShowActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);

        Intent intent = getIntent();

        setTitle(intent.getStringExtra("title"));

        WebView content = (WebView) findViewById(R.id.item_content);
        content.setWebViewClient(new WebViewClient());
        content.loadUrl(intent.getStringExtra("link"));
    }
}