package com.example.mobapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


public class FairyTaleInspection extends AppCompatActivity implements ShowPopup.PopupAction {

    public static final String FAIRYTALE_ID = "FAIRYTALE_ID";
    private int id;
    private ViewFlipper viewFlipper;
    private Fairytale fairytale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id = (Integer) getIntent().getExtras().get(FAIRYTALE_ID);

        fairytale = Fairytale.fairytales[id];

        try {
            fairytale.subscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }

        com.example.mobapp.databinding.ActivityFairyTaleInspectionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fairy_tale_inspection);
        binding.setActivity(this);
        binding.setData(fairytale);
        System.out.println("On create was called");

        System.out.println("view : ");
        this.viewFlipper =  findViewById(R.id.fairytaleImageView);


    }

    public void Next() {
        if (fairytale.getStep() + 1 >= fairytale.getMaxStep())
            popup();
        else {
            try {
                fairytale.nextStep();
                viewFlipper.setDisplayedChild(fairytale.getIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void popup() {
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
        System.out.println("resetting");
        this.fairytale.reset();
        super.onDestroy();
    }


    @Override
    public void performAction() {
        MQTTManager.getManager().publishMessage(MainActivity.topicLocation + fairytale.getTopic(), "2");
        MQTTManager.getManager().publishMessage(MainActivity.topicLocation + fairytale.getTopic() + "/reset", " ");
        this.finish();
    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.finish();
    }
}