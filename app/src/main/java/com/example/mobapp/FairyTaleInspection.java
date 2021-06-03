package com.example.mobapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.mobapp.fairytales.Fairytale;


public class FairyTaleInspection extends AppCompatActivity implements ShowPopup.PopupAction{

    public static final String FAIRYTALE_ID = "FAIRYTALE_ID";
    private int id;
    private Fairytale fairytale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id =(Integer) getIntent().getExtras().get(FAIRYTALE_ID);

        Fairytale fairyTale = Fairytale.fairytales[id];

        this.fairytale = fairyTale;

        com.example.mobapp.databinding.ActivityFairyTaleInspectionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fairy_tale_inspection);
        binding.setActivity(this);
        binding.setData(fairyTale);

        findViewById(R.id.inspector_nextbutton).setOnClickListener(this::onNextClick);
    }

    private void onNextClick(View view) {
        ViewStub stub = (ViewStub)findViewById(R.id.inspection_main_content);
        stub.setLayoutResource(this.fairytale.getLayout());
        View infalted = stub.inflate();
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