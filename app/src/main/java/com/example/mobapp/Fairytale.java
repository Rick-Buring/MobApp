package com.example.mobapp;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;

public class Fairytale extends BaseObservable implements Serializable {

    private final String topic;
    private final String name;
    private final String location;
    private final String timeToComplete;
    private final String description;
    private final int image;
    private int step;

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
        this.step = 1;
        this.topic = topic;
        this.color = R.color.transparend;
        this.clickable = true;
    }

    public void nextStep(){
        step++;
        //todo update variable

        notifyPropertyChanged(BR.step);
    }

    private void setColor(int color){
        this.color = color;
        notifyPropertyChanged(BR.color);
    }

    @Bindable
    public int getColor(){return this.color;}

    @Bindable
    public String getStep() {
        //todo make a not hardcoded string
        return "stap: " + step;
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
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground,"TheWulfAndThreePigs"),
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground, "HanselAndGretel"),
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground, "Cinderella")
    };

    public String getTopic() {
        return this.topic;
    }

    public void MessageReceived(MqttMessage message) {
        Log.d("TAG", "MessageReceived: " + message);
        if(message.toString().equals("true")){
            this.setColor(R.color.transparend);
            this.clickable = true;
        }else if(message.toString().equals("false")){
            this.setColor(R.color.disabledColor);
            this.clickable = false;
        }
    }
}
