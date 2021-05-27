package com.example.mobapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //todo connect to MQTT server

        new Handler(Looper.getMainLooper()).postDelayed(() -> startActivity(new Intent(this, MainActivity.class)), 3000);

    }
}
