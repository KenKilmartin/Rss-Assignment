package com.example.ken.assignment_rss_reader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RSSFeedActivity extends AppCompatActivity {


    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<>();

    RSSParser rssParser = new RSSParser();
    Toolbar toolbar;
    int finalValue;
    ListView lv;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String>description = new ArrayList<>();
    ArrayList<String>pubDate= new ArrayList<>();
    ArrayList<String>link = new ArrayList<>();
    ArrayList<Bitmap>images = new ArrayList<>();
//    ImageView imageView;
   Bitmap bimage;
    Button backbutton;

//    final SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.myapp.namePrefereance",Context.MODE_PRIVATE);
//    final SharedPreferences.Editor editor = sharedPreferences.edit();

    List<RSSItem> rssItems = new ArrayList<>();

//    String savedHeadline;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_feed);
//        imageView = findViewById(R.id.image);
        backbutton = findViewById(R.id.backbtn);

        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.myapp.namePrefereance",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        String rss_link = getIntent().getStringExtra("rssLink");
        String numberOfFeed = getIntent().getStringExtra("numberEntered");
        finalValue=Integer.parseInt(numberOfFeed);
        new LoadRSSFeedItems().execute(rss_link);

        // creates the list view
        lv = findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent in = new Intent(getApplicationContext(), BrowserActivity.class);
                String page_url = ((TextView) view.findViewById(R.id.page_url)).getText().toString();
                in.putExtra("url", page_url);
                startActivity(in);
            }
        });
        // when we click the back button on the second page this clears the values that has been entered so it will go back to the first page
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
        protected void onPostExecute(String result) {
            lv.setAdapter(new MyAdapter(RSSFeedActivity.this, R.layout.rss_item_list_row, title, description, pubDate, link, images));
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


//          loop through the rss with given number
            for(int i = 0; i<finalValue; i++){


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

                bimage = null;
                try {
                    InputStream in = new java.net.URL(imageURL).openStream();
                    bimage = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error Message", e.getMessage());
                    e.printStackTrace();
                }


                title.add(rssItems.get(i).title);
                description.add(rssItems.get(i).description);
                pubDate.add(rssItems.get(i).pubdate);
                link.add(rssItems.get(i).link);
                images.add(bimage);


            }

            return null;
        }


    }

    private class MyAdapter extends ArrayAdapter<String> {
        private int layoutResourceId;
        private String[] title = new String[]{};
        private String[] description = new String[]{};
        private String[] pubDate = new String[]{};
        private String[] link = new String[]{};
        private Bitmap[] images = new Bitmap[]{};
        public MyAdapter(Context context, int layoutId, ArrayList<String> title, ArrayList<String> description, ArrayList<String> pubDate, ArrayList<String> link, ArrayList<Bitmap>images) {
            super(context, layoutId, title);
            this.layoutResourceId = layoutId;
            this.title = title.toArray(new String[0]);
            this.description = description.toArray(new String[0]);
            this.pubDate = pubDate.toArray(new String[0]);
            this.link = link.toArray(new String[0]);
            this.images = images.toArray(new Bitmap[0]);
        }

        @Override
        public View getView(int index, View row, ViewGroup parent){
            row = getLayoutInflater().inflate(layoutResourceId, parent, false);
            TextView titleTV = row.findViewById(R.id.title);
            TextView descriptionTV = row.findViewById(R.id.desc);
            TextView pubDateTV = row.findViewById(R.id.pub_date);
            TextView linkTV = row.findViewById(R.id.page_url);
            ImageView imageView = row.findViewById(R.id.image);

            titleTV.setText(title[index]);
            descriptionTV.setText(description[index]);
            pubDateTV.setText(pubDate[index]);
            linkTV.setText(link[index]);
            imageView.setImageBitmap(images[index]);

            return row;
        }
    }
}
