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

    @Override
    public void subscribe() throws Exception {
        MQTTManager.getManager().subscribeToTopic(MainActivity.topicLocation + this.getTopic() + "/blower/total" );
    }

    @Override
    public void nextStep() {
        setFeedback("");
        notifyPropertyChanged(BR.feedback);
        if (getStep() + 1 >= views.size())
            setStep(0);
        else
            setStep(getStep() + 1);

        switch (getStep()) {
            case 3:
            case 7:
            case 5:
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/next", " ");
                break;
            case 4:
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/house/1", " ");
                break;
            case 6:
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/house/2", " ");
                break;
            case 8:
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/house/3", " ");
                break;
        }
    }
}
