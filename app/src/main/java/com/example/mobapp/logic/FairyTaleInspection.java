package com.example.mobapp.logic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.mobapp.MainActivity;
import com.example.mobapp.R;
import com.example.mobapp.fairytale.Fairytale;
import com.example.mobapp.fairytale.viewFlipperCallback;


public class FairyTaleInspection extends AppCompatActivity implements ShowPopup.PopupAction, viewFlipperCallback {

    public static final String FAIRYTALE_ID = "FAIRYTALE_ID";
    private int id;
    private ViewFlipper viewFlipper;
    private Fairytale fairytale;

    /**
     * onCreate method for the Inspection screen
     * @param savedInstanceState  The saved data from the screen that called this intent
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the fairytale that the user clicked
        this.id = (Integer) getIntent().getExtras().get(FAIRYTALE_ID);
        fairytale = Fairytale.fairytales[id];

        // subscribing to the topics of the selected fairytale
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

    /**
     * Load the next step in the story
     */
    public void Next() {
        if (fairytale.getStep() + 1 >= fairytale.getMaxStep())
            popup();
        else {

            try {
                fairytale.nextStep(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Shows a popup confirming you to quit
     */
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

    @Override
    public void flipperNext() {
        this.viewFlipper.showNext();
    }

    @Override
    public void flipperPrevious() {
        this.viewFlipper.showPrevious();
    }

    @Override
    public void flipperSkipOne() {
        this.viewFlipper.setDisplayedChild((this.viewFlipper.getDisplayedChild() + 2) % this.viewFlipper.getChildCount());
    }
}