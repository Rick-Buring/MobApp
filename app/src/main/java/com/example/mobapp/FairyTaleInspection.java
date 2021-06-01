package com.example.mobapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


public class FairyTaleInspection extends AppCompatActivity{

    public static final String FAIRYTALE_ID = "FAIRYTALE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id =(Integer) getIntent().getExtras().get(FAIRYTALE_ID);

        Fairytale fairytale = Fairytale.fairytales[id];

        com.example.mobapp.databinding.ActivityFairyTaleInspectionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fairy_tale_inspection);
        binding.setActivity(this);
        binding.setData(fairytale);
    }

    public void popup(){
        new ShowPopup(new View(getApplicationContext()),(LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE), this);
    }

    @Override
    public void onBackPressed() {
        popup();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}