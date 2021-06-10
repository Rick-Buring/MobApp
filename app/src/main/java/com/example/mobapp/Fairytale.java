package com.example.mobapp;

import android.util.Log;
import android.widget.ViewFlipper;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class Fairytale extends BaseObservable implements Serializable {

    private final String name;
    private final String location;
    private final String timeToComplete;
    private final String description;
    private final String topic;
    private int available;

    private boolean isStory;

    @Bindable
    public int getAvailable() {
        return available;
    }

    private int maxStep;

    @Bindable
    public int getMaxStep() {
        return maxStep;
    }

    public void setMaxStep(int maxStep) {
        this.maxStep = maxStep;
        notifyPropertyChanged(BR.maxStep);
    }

    private int step;
    private int image;
    private boolean clickable;
    private String feedback = "";
    private int storyText;

    @Bindable
    public int getStoryText() {
        return storyText;
    }

    public void setStoryText(int storyText) {
        this.storyText = storyText;
        notifyPropertyChanged(BR.storyText);
    }

    @Bindable
    public boolean isStory() {
        return isStory;
    }

    public void setStory(boolean story) {
        isStory = story;
        notifyPropertyChanged(BR.story);
    }

    public void reset() {
        this.step = -1;
        try {
            nextStep();
            notifyPropertyChanged(BR._all);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Fairytale(String name, String location, String timeToComplete, String description, int image, String topic) {
        this.name = name;
        this.location = location;
        this.timeToComplete = timeToComplete;
        this.description = description;
        this.image = image;
        this.step = 0;
        this.topic = topic;
        this.available = R.string.press_button;
        this.clickable = false;
        this.setStory(true);
    }

    public static Fairytale[] fairytales = new Fairytale[]{
            new FairytaleTheePigs(),
            new Fairytale("Hansel And Gretel", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground, "HanselAndGretel"),
            new Fairytale("Cinderella", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground, "Cinderella")
    };

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
            feedback = message.toString();
            notifyPropertyChanged(BR.feedback);
        }
    }

    public void nextStep() throws Exception {
        throw new Exception("Not implemented");
    }

    public void subscribe() throws Exception {
        throw new Exception("Not implemented");
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
    public String getName() {
        return name;
    }

    @Bindable
    public String getLocation() {
        return location;
    }

    @Bindable
    public String getTimeToComplete() {
        return timeToComplete;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    @Bindable
    public int getStep() {
        return this.step;
    }

    @Bindable
    public String getFeedback() {
        return feedback;
    }

    public String getTopic() {
        return topic;
    }
    //endregion

    private void setClickable(boolean clickable) {
        this.clickable = clickable;
        notifyPropertyChanged(BR.clickable);
    }

    public void setStep(int step) {
        this.step = step;
        notifyPropertyChanged(BR.step);
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
        notifyPropertyChanged(BR.feedback);
    }
}
