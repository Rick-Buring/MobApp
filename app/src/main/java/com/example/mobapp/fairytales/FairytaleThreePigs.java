package com.example.mobapp.fairytales;

import com.example.mobapp.R;
import com.example.mobapp.step.Step;

public class FairytaleThreePigs extends Fairytale{

    public FairytaleThreePigs() {
        super("The wulf and the three pigs",
                "Point 3 on map",
                "3 minutes",
                "description",
                R.drawable.fairytale_grotebozewolf3,
                "ti/1.4/b1/availability/TheWulfAndThreePigs");

        this.steps.put(1, new Step(R.layout.test));
    }

}
