package com.example.mobapp;

import java.util.ArrayList;

public class FairytaleTheePigs extends Fairytale {

    private final stepClass[] stepClasses;

    public FairytaleTheePigs() {
        super("The wulf and the three pigs",
                "At point 3 on map",
                "3 minutes",
                "A nice tale", R.drawable.fairytale_biggetjes,
                "TheWulfAndThreePigs");

        this.stepClasses = new stepClass[]{
                new stepClass(R.string.fairytale_page_0, true, 0),
                new stepClass(R.string.fairytale_page_1, true, 1),
                new stepClass(R.string.fairytale_page_2, true, 2),
                new stepClass(R.string.fairytale_page_2, false, 3),
                new stepClass(R.string.fairytale_page_3, true, 4),
                new stepClass(R.string.fairytale_page_3, false, 1),
                new stepClass(R.string.fairytale_page_4, true, 2),
                new stepClass(R.string.fairytale_page_4, false, 5),
                new stepClass(R.string.fairytale_page_5, true, 5),
                new stepClass(R.string.fairytale_page_6, true, 0)
        };


        setMaxStep(10);
        setCurrentStep(this.stepClasses[0]);
        this.locked = false;
    }

    @Override
    public void subscribe() throws Exception {
        MQTTManager.getManager().subscribeToTopic(MainActivity.topicLocation + this.getTopic() + "/blower/total");
    }

    private boolean locked;

    @Override
    public void nextStep() {
        if(locked && getFeedback() < 100)
            return;
        setFeedback(0);
        setStep(getStep() + 1);
        System.out.println("Calling from reset: " + getStep());

        setCurrentStep(this.stepClasses[getStep()]);
        notifyPropertyChanged(BR._all);

        switch (getStep()) {
            case 3:
            case 7:
            case 5:
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/next", " ");
                this.locked = true;
                break;
            case 4:
                this.locked = false;
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/house/1", " ");
                break;
            case 6:
                this.locked = false;
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/house/2", " ");
                break;
            case 8:
                this.locked = false;
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/house/3", " ");
                break;
            case 9:
                break;
        }
    }
}
