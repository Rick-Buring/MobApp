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

        try {
            fairytale.subscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }

        com.example.mobapp.databinding.ActivityFairyTaleInspectionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fairy_tale_inspection);
        binding.setActivity(this);
        binding.setData(fairytale);

        this.viewFlipper = findViewById(R.id.flipper);
        viewFlipper.setInAnimation(this, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
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
        if (fairytale.getStep() + 1 >= fairytale.maxStep())
            popup();
        else {
            try {
                fairytale.nextStep();
                viewFlipper.showNext();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(fairytale.getText());
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
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}