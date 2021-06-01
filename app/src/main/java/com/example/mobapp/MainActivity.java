package com.example.mobapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements FairyTaleAdapter.OnItemClickListener {

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
        Intent intent = new Intent(this, FairyTaleInspection.class);
        intent.putExtra(FairyTaleInspection.FAIRYTALE_ID, clickedPosition);
        startActivity(intent);
    }
}