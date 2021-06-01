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
        MQTTManager manager = new MQTTManager(this);


        new Handler(Looper.getMainLooper()).postDelayed(()-> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.putExtra(MainActivity.MQTTTag, manager);
            startActivity(intent);
            this.finish();
        }, 3000);


    }


}
