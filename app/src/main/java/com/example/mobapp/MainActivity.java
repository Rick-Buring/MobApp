package com.example.mobapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements FairyTaleAdapter.OnItemClickListener {

    public static final String MQTTTag = "MQTTTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        FairyTaleAdapter fairytaleAdapter = new FairyTaleAdapter(this);
        recyclerView.setAdapter(fairytaleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onItemClick(int clickedPosition) {
        //todo start activity with the right object
        Intent intent = new Intent();
//        intent.putExtra(FairyTaleActivity.FAIRYTALE_ID, clickedPosition);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        // Disconnect from the MQTT broker when the activity is closed
        Log.d("MainActivity", "Disconnecting from MQTT broker");
        MQTTManager.getManager().disconnectFromBroker();
        super.onStop();
    }
}