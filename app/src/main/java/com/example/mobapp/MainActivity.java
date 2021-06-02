package com.example.mobapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements FairyTaleAdapter.OnItemClickListener, ShowPopup.PopupAction {

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
        this.clickedPosition = clickedPosition;
        new ShowPopup("Weet je zeker dat je dit sprookje wilt starten?",
                "ja",
                "nee",
                new View(getApplicationContext()), getSystemService(LayoutInflater.class),
                this
        ).show();
    }

    private int clickedPosition;

    @Override
    public void performAction() {
        Intent intent = new Intent(this, FairyTaleInspection.class);
        intent.putExtra(FairyTaleInspection.FAIRYTALE_ID, this.clickedPosition);
        startActivity(intent);
    }
}