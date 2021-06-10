package com.example.mobapp;

public class FairytaleTheePigs extends Fairytale {

    public FairytaleTheePigs() {
        super("The wulf and the three pigs",
                "At point 3 on map",
                "3 minutes",
                "A nice tale", R.drawable.fairytale_biggetjes,
                "TheWulfAndThreePigs");

        setMaxStep(10);
        setStory(true);
        setStoryText(R.string.fairytale_page_0);
    }

    @Override
    public void subscribe() throws Exception {
        MQTTManager.getManager().subscribeToTopic(MainActivity.topicLocation + this.getTopic() + "/blower/total");
    }

    @Override
    public void nextStep() {
        setFeedback(0);
        setStep(getStep() + 1);
        System.out.println("Calling from reset: " + getStep());

        switch (getStep()) {
            case 0:
                setStory(true);
                setStoryText(R.string.fairytale_page_0);
                break;
            case 1:
                setStoryText(R.string.fairytale_page_1);
                break;
            case 2:
                setStoryText(R.string.fairytale_page_2);
                break;
            case 3:
                setStory(false);
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/next", " ");
                break;
            case 4:
                setStory(true);
                setStoryText(R.string.fairytale_page_3);
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/house/1", " ");
                break;
            case 5:
                setStory(false);
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/next", " ");
                break;
            case 6:
                setStory(true);
                setStoryText(R.string.fairytale_page_4);
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/house/2", " ");
                break;
            case 7:
                setStory(false);
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/next", " ");
                break;
            case 8:
                setStory(true);
                setStoryText(R.string.fairytale_page_5);
                MQTTManager.getManager().publishMessage(MainActivity.topicLocation + getTopic() + "/house/3", " ");
                break;
            case 9:
                setStoryText(R.string.fairytale_page_6);
                break;
        }
    }
}
