package com.example.mobapp;

import android.widget.LinearLayout;

import java.util.HashMap;

public class FairytaleTheePigs extends Fairytale {

    public FairytaleTheePigs() {
        super("The wulf and the three pigs",
                "At point 3 on map",
                "3 minutes",
                "A nice tale", R.drawable.fairytale_grotebozewolf3,
                "ti/1.4/b1/availability/TheWulfAndThreePigs");

        this.steps = new HashMap<Integer, Step>();


        this.steps.put(1, new Step(R.layout.activity_splash_screen));
    }

}
