package com.example.mobapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobapp.fairytale.Fairytale;
import com.example.mobapp.logic.FairyTaleInspection;
import com.example.mobapp.logic.MQTTManager;
import com.example.mobapp.logic.ShowPopup;

public class MainActivity extends AppCompatActivity implements FairyTaleAdapter.OnItemClickListener, ShowPopup.PopupAction {

    public static final String topicLocation = "ti/1.4/b1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycleView);
        FairyTaleAdapter fairytaleAdapter = new FairyTaleAdapter(this);
        recyclerView.setAdapter(fairytaleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Creating a new MQTT manager
        MQTTManager manager = MQTTManager.getManager();

        // subscribing to all the ping-locations of the fairy-tales
        for (Fairytale tale : Fairytale.fairytales) {
            manager.subscribeToTopic(topicLocation + tale.getTopic());
        }

        // Ping the broker for all open actions
        manager.publishMessage(topicLocation + "availability/request", " ");
    }

    /**
     * Callback for a new popup to ask the user for confirmation
     * @param clickedPosition  The action the user clicked on
     */
    @Override
    public void onItemClick(int clickedPosition) {
        if (!Fairytale.fairytales[clickedPosition].isClickable())
            return;
        this.clickedPosition = clickedPosition;
        new ShowPopup(getString(R.string.start_fairy_popup),
                "ja",
                "nee",
                new View(getApplicationContext()), getSystemService(LayoutInflater.class),
                this
        ).show();
    }

    private int clickedPosition;

    /**
     * Performs the action by the popup
     */
    @Override
    public void performAction() {
        if (Fairytale.fairytales[clickedPosition].isClickable()) {

            // publish the step to MQTT
            MQTTManager.getManager().publishMessage(topicLocation + Fairytale.fairytales[clickedPosition].getTopic(), "1");

            // update the (custom) lastw ill message to include the new correct step
            MQTTManager.getManager().publishMessage(topicLocation + Fairytale.fairytales[clickedPosition].getTopic() + "/lastwill",
                    MQTTManager.UID.toString());

            // starting new intent to the FairyTaleInspection
            Intent intent = new Intent(this, FairyTaleInspection.class);
            intent.putExtra(FairyTaleInspection.FAIRYTALE_ID, this.clickedPosition);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    /**
     * Closes the connection, starts the animation
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}