package com.example.mobapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements FairyTaleAdapter.OnItemClickListener, ShowPopup.PopupAction {

    public static final String topicLocation = "ti/1.4/b1/availability/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        FairyTaleAdapter fairytaleAdapter = new FairyTaleAdapter(this);
        recyclerView.setAdapter(fairytaleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        MQTTManager manager = MQTTManager.getManager();

        for (Fairytale tale: Fairytale.fairytales) {
            manager.subscribeToTopic(topicLocation + tale.getTopic());
        }

        manager.publishMessage(topicLocation + "request", " ");
    }

    @Override
    public void onItemClick(int clickedPosition) {
        if(!Fairytale.fairytales[clickedPosition].isClickable())
            return;
        this.clickedPosition = clickedPosition;
        new ShowPopup( getString(R.string.start_fairy_popup),
                "ja",
                "nee",
                new View(getApplicationContext()), getSystemService(LayoutInflater.class),
                this
        ).show();
    }

    private int clickedPosition;

    @Override
    public void performAction() {
        MQTTManager.getManager().publishMessage(topicLocation + Fairytale.fairytales[clickedPosition].getTopic(), "1");
        Intent intent = new Intent(this, FairyTaleInspection.class);
        intent.putExtra(FairyTaleInspection.FAIRYTALE_ID, this.clickedPosition);
        startActivity(intent);
    }
}