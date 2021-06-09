package com.example.mobapp;

public class FairytaleTheePigs extends Fairytale {

    public FairytaleTheePigs() {
        super("The wulf and the three pigs",
                "At point 3 on map",
                "3 minutes",
                "A nice tale", R.drawable.fairytale_grotebozewolf3,
                "TheWulfAndThreePigs");

        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_1));
        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_2));
        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_3));

        views.add(new fairytaleStepView(R.layout.story_blow_layout));
        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_4));

        views.add(new fairytaleStepView(R.layout.story_blow_layout));
        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_5));

        views.add(new fairytaleStepView(R.layout.story_blow_layout));
        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_6));
        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_7));

    }
    

}
