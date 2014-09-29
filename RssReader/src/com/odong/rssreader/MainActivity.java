package com.odong.rssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.odong.rssreader.store.Storage;
import com.odong.rssreader.utils.Rss;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshFeedList();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_add_feed:
                onAddFeed();
                break;
            case R.id.action_help:
                onMessage(R.string.help_title, R.string.help_body, R.drawable.ic_action_about);
                break;
            case R.id.action_about:
                onMessage(R.string.about_title, R.string.about_body, R.drawable.ic_action_help);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initFeedList();

    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dlg_exit_message)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.btn_no, null)
                .show();
    }

    private void onMessage(int title, int body, int icon) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.putExtra("icon", icon);
        startActivity(intent);
    }

    private void onAddFeed() {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setMessage(R.string.dlg_addFeed_title)
                .setView(input)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message message) {
                                String msg = (String) message.obj;
                                if (msg.equals("SUCCESS")) {
                                    refreshFeedList();
                                } else {
                                    new AlertDialog.Builder(MainActivity.this).setTitle(R.string.dlg_error_title)
                                            .setMessage(msg)
                                            .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            }
                        };
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = handler.obtainMessage();
                                try {
                                    Rss rss = new Rss(input.getText().toString());
                                    Storage store = new Storage(getApplicationContext());
                                    int fid = store.addFeed(rss.getChannel());
                                    store.addItems(fid, rss.getItemList());
                                    msg.obj = "SUCCESS";
                                } catch (XmlPullParserException e) {
                                    msg.obj = getString(R.string.exception_xml_parser);
                                } catch (IOException e) {
                                    msg.obj = getString(R.string.exception_network);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    msg.obj = e.getMessage();
                                }

                                handler.sendMessage(msg);
                            }
                        }).start();


                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }


    private void initFeedList() {

        ListView lvFeeds = (ListView) findViewById(R.id.lv_feeds);
        lvFeedItems = new ArrayList<Map<String, String>>();
        lvFeedIds = new ArrayList<Integer>();


        lvFeedAdapter = new SimpleAdapter(this, lvFeedItems, R.layout.list_item, new String[]{"title", "summary"}, new int[]{R.id.item_title, R.id.item_summary});
        lvFeeds.setAdapter(lvFeedAdapter);

        //lv.setClickable(true);
        lvFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                intent.putExtra("id", lvFeedIds.get(position));
                intent.putExtra("title", lvFeedItems.get(position).get("title"));
                startActivity(intent);
            }
        });

        lvFeeds.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.dlg_delete_title)
                        .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Storage s = new Storage(getApplicationContext());
                                s.delFeed(lvFeedIds.get(position));
                                refreshFeedList();
                            }
                        })
                        .setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });

        refreshFeedList();
    }

    private void refreshFeedList() {
        lvFeedItems.clear();
        lvFeedIds.clear();

        Storage storage = new Storage(this);
        storage.listFeed(new Storage.FeedCallback() {
            @Override
            public void run(int id, String title, String description, String lastSync) {
                lvFeedIds.add(id);

                Map<String, String> v = new HashMap<String, String>();
                v.put("title", title);
                v.put("summary", description);
                lvFeedItems.add(v);
            }
        });

        lvFeedAdapter.notifyDataSetChanged();
    }

    private List<Map<String, String>> lvFeedItems;
    private SimpleAdapter lvFeedAdapter;
    private List<Integer> lvFeedIds;
}
