package com.example.ken.assignment_rss_reader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText userEnteredUrl,numberEntered;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEnteredUrl= (EditText)findViewById(R.id.userEnteredUrl);
        numberEntered = (EditText)findViewById(R.id.numberEntered);




        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RSSFeedActivity.class).putExtra("rssLink", userEnteredUrl.getText().toString()).putExtra("numberEntered",numberEntered.getText().toString()));
            }
        });

    }
}
