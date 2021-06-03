package com.example.mobapp;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


public class FairyTaleInspection extends AppCompatActivity implements ShowPopup.PopupAction{

    public static final String FAIRYTALE_ID = "FAIRYTALE_ID";
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id =(Integer) getIntent().getExtras().get(FAIRYTALE_ID);

        Fairytale fairyTale = Fairytale.fairytales[id];

        com.example.mobapp.databinding.ActivityFairyTaleInspectionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fairy_tale_inspection);
        binding.setActivity(this);
        binding.setData(fairyTale);
    }

    public void popup(){
        new ShowPopup(getString(R.string.quit_fairy_popup),
                "Ja",
                "Nee",
                new View(getApplicationContext()),
                getSystemService(LayoutInflater.class),
                this)
                .show();
    }

    @Override
    public void onBackPressed() {
        popup();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void performAction() {
        MQTTManager.getManager().publishMessage(MainActivity.topicLocation + Fairytale.fairytales[id].getTopic(), "0");
        this.finish();
    }
}