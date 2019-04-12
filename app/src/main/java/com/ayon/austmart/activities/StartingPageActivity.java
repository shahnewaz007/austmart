package com.ayon.austmart.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ayon.austmart.R;

public class StartingPageActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent startingPageIntent = new Intent(StartingPageActivity.this, BaseActivity.class);
                startActivity(startingPageIntent);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }
}