package com.example.mobapp;

import android.util.Log;
import android.widget.ViewFlipper;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that handles the story of a general fairytale
 * The class handles showing and holding each step in the tale
 */
public class Fairytale extends BaseObservable implements Serializable {

    public static Fairytale[] fairytales = new Fairytale[]{
            new FairytaleTheePigs(),
            new Fairytale(R.string.hans_name, R.string.placeholder_location, R.string.placeholder_time, R.string.hans_desc, R.drawable.fairytale_hans, "HansAndGretal"),
            new Fairytale(R.string.cinderella_name, R.string.placeholder_location, R.string.placeholder_time, R.string.cinderella_desc, R.drawable.fairytale_cinderella, "Cinderella")
    };

    /**
     * Internal class stepClass
     * Represents one step of the story
     */
    public class stepClass {

        private final int text;
        private final int index;
        private final boolean isTextView;

        /**
         * Constructor for stepClass
         * @param text  the text the step holds
         * @param isTextView  if the text should be showed
         * @param index  the index of the story this class represents
         */
        public stepClass(int text, boolean isTextView, int index) {
            this.text = text;
            this.index = index;
            this.isTextView = isTextView;
        }

        /**
         * Getter for this.isTextView
         * @return  this.isTextView
         */
        public boolean isTextView() {
            return isTextView;
        }

        /**
         * Getter for this.text
         * @return  this.text
         */
        public int getText() {
            return text;
        }

        /**
         * Getter for this.index
         * @return  this.index
         */
        public int getIndex(){
            return index;
        };
    }

    private final int name;
    private final int location;
    private final int timeToComplete;
    private final int description;
    private final String topic;

    private int available;
    private int maxStep;

    private int step;
    private int image;
    private boolean clickable;
    private int feedback;

    private stepClass currentStep;

    /**
     * Constructor for Fairytale
     * @param name  The name of the tale
     * @param location  The map-location of the tale
     * @param timeToComplete  The average minutes the tale takes to complete
     * @param description  The description of the tale
     * @param image  Main image of the tale
     * @param topic  The topic of the tale
     */
    public Fairytale(int name, int location, int timeToComplete, int description, int image, String topic) {
        this.name = name;
        this.location = location;
        this.timeToComplete = timeToComplete;
        this.description = description;
        this.image = image;
        this.step = 0;
        this.topic = topic;
        this.available = R.string.press_button;
        this.clickable = false;
    }

    /**
     * Setter for this.currentStep
     * @param currentStep  the value to set currentStep to
     */
    public void setCurrentStep(stepClass currentStep) {
        this.currentStep = currentStep;
        notifyPropertyChanged(BR._all);
    }

    /**
     * Getter for the index of this.currentStep
     * @return  this.currentStep.index
     */
    public int getIndex() {
        return this.currentStep.index;
    }

    /**
     * Getter for this.available
     * @return  this.available
     */
    @Bindable
    public int getAvailable() {
        return available;
    }

    /**
     * Getter for this.maxStep
     * @return  this.maxStep
     */
    @Bindable
    public int getMaxStep() {
        return maxStep;
    }

    /**
     * Setter for this.maxStep
     * @param maxStep  the step this.maxStep needs to be set to
     */
    public void setMaxStep(int maxStep) {
        this.maxStep = maxStep;
        notifyPropertyChanged(BR.maxStep);
    }

    /**
     * Resets all the value's of the story
     */
    public void reset() {
        this.step = -1;
        try {
            nextStep(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the message that was received from the broker
     * @param message  The message received from the broker
     */
    public void MessageReceived(MqttMessage message) {
        Log.d("TAG", "MessageReceived: " + message);
        if (message.toString().equals("0")) {
            setClickable(true);
        } else if (message.toString().equals("1")) {
            setClickable(false);
            this.available = R.string.in_use;
            notifyPropertyChanged(BR.available);
        } else if (message.toString().equals("2")) {
            setClickable(false);
            this.available = R.string.press_button;
            notifyPropertyChanged(BR.available);
        } else {
            setFeedback(Integer.parseInt(message.toString()));
            notifyPropertyChanged(BR.feedback);
        }
    }

    // TODO
    public void nextStep(viewFlipperCallback flipperCallback) throws Exception {
        throw new Exception("Not implemented");
    }

    // TODO
    public void subscribe() throws Exception {
        throw new Exception("Not implemented");
    }

    private void setClickable(boolean clickable) {
        this.clickable = clickable;
        notifyPropertyChanged(BR.clickable);
    }

    public void setStep(int step) {
        this.step = step;
        notifyPropertyChanged(BR.step);
    }

    public void setFeedback(int feedback) {
        this.feedback = Math.min(feedback, 100);
        notifyPropertyChanged(BR.feedback);
    }

    //region getters

    @Bindable
    public boolean isClickable() {
        return clickable;
    }

    @Bindable
    public int getImage() {
        return image;
    }

    @Bindable
    public int getName() {
        return name;
    }

    @Bindable
    public int getLocation() {
        return location;
    }

    @Bindable
    public int getTimeToComplete() {
        return timeToComplete;
    }

    @Bindable
    public int getDescription() {
        return description;
    }

    @Bindable
    public int getStep() {
        return this.step;
    }

    @Bindable
    public int getFeedback() {
        return feedback;
    }

    @Bindable
    public boolean isStory() {
        return currentStep.isTextView;
    }

    @Bindable
    public int getStoryText() {
        return currentStep.text;
    }

    public String getTopic() {
        return topic;
    }

    //endregion
}
