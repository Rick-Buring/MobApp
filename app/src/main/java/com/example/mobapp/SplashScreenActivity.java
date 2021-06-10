package com.example.mobapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Creates the opening screen, with the logo in the centre
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Opening a new MQTTManager, it wil Establish a new connection
        new MQTTManager(this);

        // Start the animation on the screen
        new Handler(Looper.getMainLooper()).postDelayed(()-> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            this.finish();
        }, 3000);


    }


}
