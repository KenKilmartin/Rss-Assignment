package com.example.ken.assignment_rss_reader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.ken.assignment_rss_reader.RSSParser.*;


public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        List<RSSItem> rssItems = new ArrayList<>();

        Toast.makeText(context, "There is a new Rss Feed *Maybe*.",
                Toast.LENGTH_LONG).show();
        // Vibrate the mobile phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);



//        final SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.myapp.namePrefereance",Context.MODE_PRIVATE);
//        final SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        String url;
//        url = sharedPreferences.getString("Rss entered","");
//        RSSFeedActivity.LoadRSSFeedItems.execute(url);      //execute cannot be referenced from a static context
//        RSSParser rssParser = new RSSParser();
//        rssItems = rssParser.getRSSFeedItems(url);
//
//        String savedHeadline = sharedPreferences.getString("first rss","");
//        //if first title is not same as saved title, then there's a new headline
//
//        String firstHeadline = rssItems.get(0).title;
//        System.out.print(savedHeadline+firstHeadline);
//        if(!(firstHeadline).equals(savedHeadline)){
//
//            editor.putString("first rss",firstHeadline);
//            editor.apply();
//            Toast.makeText(context, "There is a new Rss Feed *Maybe*....here in rssfeedactivity ",
//                    Toast.LENGTH_LONG).show();
//        }
//
//
//
    }

}
