package com.example.mobapp.fairytale;

import android.util.Log;

import com.example.mobapp.BR;
import com.example.mobapp.logic.MQTTManager;
import com.example.mobapp.MainActivity;
import com.example.mobapp.R;

/**
 * Class that controls all the steps for the story of the three little pigs
 * Extends general class, Fairytale
 */
public class FairytaleTheePigs extends Fairytale {
    private static final String LOGTAG = FairytaleTheePigs.class.getName();

    // Array to hold all the Steps in this story
    private final stepClass[] stepClasses;

    /**
     * Main constructor for the Three little pigs tale
     */
    public FairytaleTheePigs() {
        // set all the general date for the tale
        super(R.string.wolf_name,
                R.string.wolf_location,
                R.string.wolf_time,
                R.string.wolf_desc,
                R.drawable.fairytale_biggetjes,
                "TheWulfAndThreePigs");// topic name is important, do not change

        // Initialize all the Steps that are in the story
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


        setMaxStep(this.stepClasses.length);
        setCurrentStep(this.stepClasses[0]);

        this.locked = false;
    }

    /**
     * Makes the MQTT broker subscribe to all the topics for this story
     * @throws Exception  Exception thrown by this method
     */
    @Override
    public void subscribe() throws Exception {
        MQTTManager.getManager(null).subscribeToTopic(MainActivity.topicLocation + this.getTopic() + "/blower/total");
    }

    private boolean locked;

    /**
     * Makes the broker perform the correct action of the current step
     * @param flipperCallback  The callback object to tell to go to the next image to
     */
    @Override
    public void nextStep(viewFlipperCallback flipperCallback) {
        if(this.locked && getFeedback() < 100)
            return;
        setFeedback(0);
        setStep(getStep() + 1);
        Log.d(LOGTAG , "Calling from reset: " + getStep());

        setCurrentStep(this.stepClasses[getStep()]);
        notifyPropertyChanged(BR._all);

        switch (getStep()) {
            case 2:
                flipperCallback.flipperSkipOne();
                break;
            case 3:
                flipperCallback.flipperNext();
                // unlocking the blowing of the next house
                MQTTManager.getManager(null).publishMessage(MainActivity.topicLocation + getTopic() + "/next", " ");
                this.locked = true;
                break;
            case 4:
                flipperCallback.flipperNext();
                this.locked = false;
                // Lower the first house
                MQTTManager.getManager(null).publishMessage(MainActivity.topicLocation + getTopic() + "/house/1", " ");
                break;
                
            case 5:
                flipperCallback.flipperPrevious();
                // unlocking the blower again
                MQTTManager.getManager(null).publishMessage(MainActivity.topicLocation + getTopic() + "/next", " ");
                this.locked = true;
                break;
            case 6:
                this.locked = false;
                // lower the second house
                MQTTManager.getManager(null).publishMessage(MainActivity.topicLocation + getTopic() + "/house/2", " ");
                flipperCallback.flipperNext();
                break;
            case 7:
                this.locked = true;
                // unlocking the blower again
                MQTTManager.getManager(null).publishMessage(MainActivity.topicLocation + getTopic() + "/next", " ");
                flipperCallback.flipperPrevious();
                break;
            case 8:
                flipperCallback.flipperSkipOne();
                this.locked = false;
                // shake the last house
                MQTTManager.getManager(null).publishMessage(MainActivity.topicLocation + getTopic() + "/house/3", " ");
                break;
            case 9:
                flipperCallback.flipperNext();
                break;
        }
    }
}
