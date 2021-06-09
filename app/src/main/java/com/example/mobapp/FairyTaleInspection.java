package com.example.mobapp;

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

        com.example.mobapp.databinding.ActivityFairyTaleInspectionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fairy_tale_inspection);
        binding.setActivity(this);
        binding.setData(fairytale);


        this.viewFlipper = findViewById(R.id.flipper);
        fairytale.views.forEach(fairytaleStep -> {
            View inflatedView = LayoutInflater.from(this).inflate(fairytaleStep.getView(), null);

            ViewDataBinding bind = DataBindingUtil.bind(inflatedView);
            if (bind != null) {
                bind.setVariable(BR.data, fairytale);
                bind.executePendingBindings();
            }

            this.viewFlipper.addView(inflatedView);
        });
    }

    public void Next() {
        fairytale.nextStep();
        System.out.println(fairytale.getText());
        viewFlipper.showNext();
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
        super.onDestroy();
    }

    @Override
    public void performAction() {
        MQTTManager.getManager().publishMessage(MainActivity.topicLocation + Fairytale.fairytales[id].getTopic(), "true");
        this.finish();
    }
}