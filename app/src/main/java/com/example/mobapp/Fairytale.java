package com.example.mobapp;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Fairytale extends BaseObservable implements Serializable {

    private final String topic;
    private final String name;
    private final String location;
    private final String timeToComplete;
    private final String description;
    private final int image;
    private int step;
    public ArrayList<Integer> views;

    @Bindable
    public boolean isClickable() {
        return clickable;
    }

    private boolean clickable;

    private int color;

    public Fairytale(String name, String location, String timeToComplete, String description, int image, String topic) {
        this.name = name;
        this.location = location;
        this.timeToComplete = timeToComplete;
        this.description = description;
        this.image = image;
        this.step = 0;
        this.topic = topic;
        this.color = R.color.transparent;
        this.clickable = true;
        this.views = new ArrayList<>();
    }

    @Bindable
    public abstract int getText();

    public void nextStep() {
        if (step >= views.size())
            step = 0;
        else
            step++;
        //todo update variable
        notifyPropertyChanged(BR.stepString);
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    public int getColor() {
        return this.color;
    }

    public int getStepInt() {
        return this.step;
    }

    @Bindable
    public String getStepString() {
        //todo make a not hardcoded string
        return "stap: " + (step );
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

    public static Fairytale[] fairytales = new Fairytale[]{
            new FairytaleTheePigs(),
//            new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground, "HanselAndGretel"),
//            new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground, "Cinderella")
    };

    public String getTopic() {
        return this.topic;
    }

    private void setClickable(boolean clickable) {
        this.clickable = clickable;
        notifyPropertyChanged(BR.clickable);
    }

    public void MessageReceived(MqttMessage message) {
        Log.d("TAG", "MessageReceived: " + message);
        if (message.toString().equals("true")) {
            setClickable(true);
        } else if (message.toString().equals("false")) {
            setClickable(false);
        }
    }


}
