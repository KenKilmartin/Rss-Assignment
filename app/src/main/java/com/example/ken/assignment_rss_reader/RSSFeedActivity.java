package com.example.ken.assignment_rss_reader;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
    Button backbutton;

//    final SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.myapp.namePrefereance",Context.MODE_PRIVATE);
//    final SharedPreferences.Editor editor = sharedPreferences.edit();

    List<RSSItem> rssItems = new ArrayList<>();
    private static String TAG_TITLE = "title";
    private static String TAG_DESC = "description";
    private static String TAG_IMAGE = "image";
    private static String TAG_LINK = "link";
    private static String TAG_PUB_DATE = "pubDate";
//    String savedHeadline;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_feed);
        imageView = findViewById(R.id.image);
        backbutton = findViewById(R.id.backbtn);

        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.myapp.namePrefereance",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


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
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RSSFeedActivity.this, MainActivity.class));
                editor.putString(getString(R.string.userStringEntered),"");
                editor.putString(getString(R.string.userIntEntered),"");
                editor.commit();
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


//            savedHeadline = sharedPreferences.getString(getString(R.string.userStringEntered),"");
//            //if first title is not same as saved title, then theres a new headline
//            if(!rssItems.get(0).title.equals(savedHeadline)){
//                String firstHeadline = rssItems.get(0).title;
//                editor.putString(getString(R.string.firstRSS),firstHeadline);
//                editor.commit();
//                Toast.makeText(RSSFeedActivity.this, "There is a new Rss Feed *Maybe*.",
//                        Toast.LENGTH_LONG).show();
//            }


//            trying to loop through the rss with given number
            for(int i = 0; i<finalValue; i++){
                // creating new HashMap11
                if (rssItems.get(i).description.toString().equals(""))
                    break;
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value

                String givenDateString = rssItems.get(i).pubdate.trim();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
                try {
                    Date mDate = sdf.parse(givenDateString);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy - hh:mm a", Locale.ENGLISH);
                    rssItems.get(i).pubdate = sdf2.format(mDate);

                } catch (ParseException e) {
                    e.printStackTrace();

                }
                String imageURL = rssItems.get(i).image;

                //TODO: trying to convert imageURL to a bitmap image. Dont know how to set the image view to the image
                //
                bimage = null;
                try {
                    InputStream in = new java.net.URL(imageURL).openStream();
                    bimage = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error Message", e.getMessage());
                    e.printStackTrace();
                }
                map.put(TAG_TITLE, rssItems.get(i).title);
                map.put(TAG_DESC, rssItems.get(i).description);
                map.put(TAG_IMAGE, imageURL);
                map.put(TAG_LINK,rssItems.get(i).link);
                map.put(TAG_PUB_DATE, rssItems.get(i).pubdate);

                // adding HashList to ArrayList
                rssItemList.add(map);

            }



            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            RSSFeedActivity.this,
                            rssItemList, R.layout.rss_item_list_row,

//                            new Bitmap[]{bimage},
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
