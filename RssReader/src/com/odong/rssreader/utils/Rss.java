package com.odong.rssreader.utils;

import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 14-9-28.
 */
public class Rss {
    public class Item {
        public String pubDate;
        public String title;
        public String description;
        public String link;
    }

    public class Channel {
        public String url;
        public String title;
        public String description;
    }

    public Rss(String url) throws XmlPullParserException, IOException {

        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(new URL(url).openConnection().getInputStream(), null);
        parser.nextTag();

        itemList = new ArrayList<Item>();
        channel = new Channel();
        channel.url = url.trim();

        parser.require(XmlPullParser.START_TAG, null, "rss");
        parser.nextTag();
        readChannel(parser);
        parser.require(XmlPullParser.END_TAG, null, "rss");
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public Channel getChannel() {
        return channel;
    }

    private void readChannel(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "channel");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                channel.title = readString(parser, "title");
            } else if (name.equals("description")) {
                channel.description = readString(parser, "description");
            } else if (name.equals("item")) {
                readItem(parser);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, null, "channel");
        parser.nextTag();
    }

    private void skip(XmlPullParser parser) throws IOException, XmlPullParserException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        for (int depth = 1; depth != 0; ) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }

    }

    private void readItem(XmlPullParser parser) throws IOException, XmlPullParserException {
        Item item = new Item();
        parser.require(XmlPullParser.START_TAG, null, "item");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                item.title = readString(parser, "title");
            } else if (name.equals("link")) {
                item.link = readString(parser, "link");
            } else if (name.equals("pubDate")) {
                item.pubDate = readString(parser, "pubDate");
            } else if (name.equals("description")) {
                item.description = readString(parser, "description");
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, null, "item");
        itemList.add(item);
        parser.nextTag();

    }


    private String readString(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, name);
        String val = "";
        if (parser.next() == XmlPullParser.TEXT) {
            val = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, name);

        return val;
    }

    private List<Item> itemList;
    private Channel channel;
}
