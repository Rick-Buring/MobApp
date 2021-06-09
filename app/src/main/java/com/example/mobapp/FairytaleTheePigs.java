package com.example.mobapp;

import com.example.mobapp.databinding.StoryBlowLayoutBinding;
import com.example.mobapp.databinding.StoryLayoutBinding;

public class FairytaleTheePigs extends Fairytale {

    public FairytaleTheePigs() {
        super("The wulf and the three pigs",
                "At point 3 on map",
                "3 minutes",
                "A nice tale", R.drawable.fairytale_grotebozewolf3,
                "TheWulfAndThreePigs");

        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_1, StoryLayoutBinding.class));
        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_2, StoryLayoutBinding.class));
        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_3, StoryLayoutBinding.class));

        views.add(new fairytaleStepView(R.layout.story_blow_layout, StoryBlowLayoutBinding.class));
        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_4, StoryLayoutBinding.class));

        views.add(new fairytaleStepView(R.layout.story_blow_layout, StoryBlowLayoutBinding.class));
        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_5, StoryLayoutBinding.class));

        views.add(new fairytaleStepView(R.layout.story_blow_layout, StoryBlowLayoutBinding.class));
        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_6, StoryLayoutBinding.class));
        views.add(new fairytaleStepView(R.layout.story_layout, R.string.fairytale_page_7, StoryLayoutBinding.class));

    }
    

}
