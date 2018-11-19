package com.example.ken.assignment_rss_reader;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RSSFeedActivity extends ListActivity {


    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<>();

    RSSParser rssParser = new RSSParser();
    Toolbar toolbar;
    int finalValue;
    ImageView imageView;
    Bitmap bimage;

    List<RSSItem> rssItems = new ArrayList<>();
    private static String TAG_TITLE = "title";
    private static String TAG_DESC = "description";
    private static String TAG_IMAGE = "image";
    private static String TAG_LINK = "link";
    private static String TAG_PUB_DATE = "pubDate";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_feed);
        imageView = findViewById(R.id.image);


        String rss_link = getIntent().getStringExtra("rssLink");
        String numberOfFeed = getIntent().getStringExtra("numberEntered");
        finalValue=Integer.parseInt(numberOfFeed);
        new LoadRSSFeedItems().execute(rss_link);

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent in = new Intent(getApplicationContext(), BrowserActivity.class);
                String page_url = ((TextView) view.findViewById(R.id.page_url)).getText().toString();
                in.putExtra("url", page_url);
                startActivity(in);
            }
        });
    }

    public class LoadRSSFeedItems extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            lp.addRule(RelativeLayout.CENTER_IN_PARENT);

        }

        @Override
        protected String doInBackground(String... args) {
            // rss link url
            String rss_url = args[0];

            // list of rss items
            rssItems = rssParser.getRSSFeedItems(rss_url);
//
//            trying to loop through the rss with given number
//            for(int i = 0; i<finalValue; i++){
//                // creating new HashMap
//                if (rssItems[i].description.toString().equals(""))
//                    break;
//                HashMap<String, String> map = new HashMap<String, String>();
//            }
            // looping through each item
            for (RSSItem item : rssItems) {
                // creating new HashMap
                if (item.description.toString().equals(""))
                    break;
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value

                String givenDateString = item.pubdate.trim();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
                try {
                    Date mDate = sdf.parse(givenDateString);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy - hh:mm a", Locale.ENGLISH);
                    item.pubdate = sdf2.format(mDate);

                } catch (ParseException e) {
                    e.printStackTrace();

                }
                String imageURL = item.image;

                //TODO:
                //trying to convert imageURL to a bitmap image. Dont know how to set the image view to the image
                bimage = null;
                try {
                    InputStream in = new java.net.URL(imageURL).openStream();
                    bimage = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error Message", e.getMessage());
                    e.printStackTrace();
                }



                map.put(TAG_TITLE, item.title);
                map.put(TAG_DESC, item.description);
                map.put(TAG_IMAGE, imageURL);
                map.put(TAG_LINK,item.link);
                map.put(TAG_PUB_DATE, item.pubdate);


                // adding HashList to ArrayList
                rssItemList.add(map);
            }

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            RSSFeedActivity.this,
                            rssItemList, R.layout.rss_item_list_row,

                            new String[]{TAG_LINK, TAG_DESC, TAG_TITLE, TAG_IMAGE, TAG_PUB_DATE},
                            new int[]{R.id.page_url, R.id.desc, R.id.title, R.id.image, R.id.pub_date});

                    // updating listview
                    setListAdapter(adapter);
                }
            });
            return null;
        }

    }
}
