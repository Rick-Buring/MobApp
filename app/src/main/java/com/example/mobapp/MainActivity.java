package com.example.mobapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements FairytaleAdapter.OnItemClickListener {

    public static final String MQTTTag = "MQTTTag";
    private MQTTManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.manager = (MQTTManager) getIntent().getExtras().get(MQTTTag);

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        FairytaleAdapter fairytaleAdapter = new FairytaleAdapter(this);
        recyclerView.setAdapter(fairytaleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onItemClick(int clickedPosition) {
        //todo start activity with the right object
    }

    @Override
    protected void onStop() {
        // Disconnect from the MQTT broker when the activity is closed
        Log.d("MainActivity", "Disconnecting from MQTT broker");
        this.manager.disconnectFromBroker();
        super.onStop();
    }
}