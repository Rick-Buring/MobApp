package com.example.mobapp;

public class FairytaleTheePigs extends Fairytale {

    public FairytaleTheePigs() {
        super("The wulf and the three pigs",
                "At point 3 on map",
                "3 minutes",
                "A nice tale", R.drawable.fairytale_grotebozewolf3,
                "ti/1.4/b1/availability/TheWulfAndThreePigs");

        views.add(R.layout.activity_splash_screen);
        views.add(R.layout.activity_main);
        views.add(R.layout.popup_window);
    }
}
