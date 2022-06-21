package com.example.superhealthyapp.activities;

/*
  Sources : https://stackoverflow.com/questions/5486789/how-do-i-make-a-splash-screen
            https://medium.com/geekculture/implementing-the-perfect-splash-screen-in-android-295de045a8dc
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.appcompat.app.AppCompatActivity;

import com.example.superhealthyapp.R;
import com.example.superhealthyapp.activities.MainActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 1000;
    private PrefManager prefManager;
    @Override

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.splash_screen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                prefManager = new PrefManager(SplashActivity.this);
                if(prefManager.isFirstTimeLaunch()) {

                    /* Create an Intent that will start the Welcome-Activity. */
                    Intent mainIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
                else
                {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();

                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

