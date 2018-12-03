package com.example.ken.assignment_rss_reader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText userEnteredUrl,numberEntered;
    String userSavedEnteredUrl = "", userSavedEnteredNumber = "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startAlert();
        userEnteredUrl= (EditText)findViewById(R.id.userEnteredUrl);
        numberEntered = (EditText)findViewById(R.id.numberEntered);

        // These are for writing what previous rss feed entered
        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.myapp.namePrefereance",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        userSavedEnteredUrl = sharedPreferences.getString(getString(R.string.userStringEntered),"");
        userSavedEnteredNumber = sharedPreferences.getString(getString(R.string.userIntEntered),"");



        if(!userSavedEnteredUrl.equals("") && !userSavedEnteredNumber.equals(""))
        {
            startActivity(new Intent(MainActivity.this, RSSFeedActivity.class).putExtra("rssLink",userSavedEnteredUrl) .putExtra("numberEntered",userSavedEnteredNumber));
        }



        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                userSavedEnteredUrl = sharedPreferences.getString(getString(R.string.userStringEntered),"");
                userSavedEnteredNumber = sharedPreferences.getString(getString(R.string.userIntEntered),"");

                System.out.println(userSavedEnteredUrl + userSavedEnteredNumber);

                editor.putString(getString(R.string.userStringEntered),userEnteredUrl.getText().toString());
                editor.putString(getString(R.string.userIntEntered),numberEntered.getText().toString());
                editor.commit();
                startActivity(new Intent(MainActivity.this, RSSFeedActivity.class).putExtra("rssLink", userEnteredUrl.getText().toString()).putExtra("numberEntered",numberEntered.getText().toString()));
            }
        });

    }
    public void startAlert() {
       int i = 10; //this is 10000 milleseconds
        Intent intent = new Intent(this, MyBroadcastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);


        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);

        Toast.makeText(this, "Alarm set in " + i + " seconds",
                Toast.LENGTH_LONG).show();
    }

}
