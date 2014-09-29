package com.odong.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.odong.rssreader.store.Storage;
import com.odong.rssreader.utils.Rss;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by flamen on 14-9-29.
 */
public class FeedAddActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_add_sctivity);

        etContent = ((EditText) findViewById(R.id.feed_add_content));
        initScanBtn();
        initAddBtn();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult ir = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (ir == null) {
            etContent.setHint(R.string.lbl_feed_hint);
        } else {
            etContent.setText(ir.getContents());
        }

    }

    private void initScanBtn() {
        findViewById(R.id.btn_feed_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator ii = new IntentIntegrator(FeedAddActivity.this);
                ii.initiateScan();
            }
        });
    }

    private void initAddBtn() {
        findViewById(R.id.btn_feed_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        String msg = (String) message.obj;
                        if (msg.equals(Constants.SUCCESS)) {
                            finish();
                        } else {
                            Constants.alert(FeedAddActivity.this, msg);
                        }
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage();
                        try {
                            Rss rss = new Rss(etContent.getText().toString());
                            Storage s = Storage.getInstance();
                            int fid = s.addFeed(rss.getChannel());
                            s.addItems(fid, rss.getItemList());
                            msg.obj = Constants.SUCCESS;
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                            msg.obj = getString(R.string.exception_xml_parser);
                        } catch (IOException e) {
                            e.printStackTrace();
                            msg.obj = getString(R.string.exception_network);
                        } catch (Exception e) {
                            e.printStackTrace();
                            msg.obj = e.getMessage();
                        }

                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }

    private EditText etContent;
}