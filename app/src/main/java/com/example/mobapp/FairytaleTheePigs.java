package com.example.mobapp;

import java.util.ArrayList;

public class FairytaleTheePigs extends Fairytale {

    private ArrayList<Integer> strings;

    public FairytaleTheePigs() {
        super("The wulf and the three pigs",
                "At point 3 on map",
                "3 minutes",
                "A nice tale", R.drawable.fairytale_grotebozewolf3,
                "ti/1.4/b1/availability/TheWulfAndThreePigs");

        views.add(R.layout.story_layout);
        views.add(R.layout.story_layout);
        views.add(R.layout.story_layout);
        views.add(R.layout.story_layout);
        views.add(R.layout.story_layout);
        views.add(R.layout.story_layout);

        this.strings = new ArrayList<>();
        this.strings.add(R.string.fairytale_page_1);
        this.strings.add(R.string.fairytale_page_2);
        this.strings.add(R.string.fairytale_page_3);
        this.strings.add(R.string.fairytale_page_4);
        this.strings.add(R.string.fairytale_page_5);
        this.strings.add(R.string.fairytale_page_6);
    }

    @Override
    public int getText() {
        return this.strings.get(super.getStepInt());
    }
}
