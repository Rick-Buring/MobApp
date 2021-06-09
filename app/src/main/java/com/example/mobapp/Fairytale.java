package com.example.mobapp;

import android.util.Log;

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

    private int step;
    private final int image;
    private boolean clickable;
    private String feedback = "";
    public ArrayList<fairytaleStepView> views;

    public void reset() {
        this.step = 0;
    }

    public class fairytaleStepView{

        private final Integer view;
        private final int text;
        private final Class<?> bindClass;


        public fairytaleStepView(Integer view, Class<?> bindClass) {
            this.view = view;
            this.bindClass = bindClass;
            this.text = R.string.blaas;
        }

        public fairytaleStepView(Integer view, int text, Class<?> bindClass) {
            this.view = view;
            this.text = text;
            this.bindClass = bindClass;
        }

        public Integer getView() {
            return view;
        }

        public int getText() {
            return text;
        }
        public Class getBindClass() {
            return bindClass;
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
        this.clickable = true;
        this.views = new ArrayList<>();
    }

    public void nextStep() {
        if (step + 1 >= views.size())
            step = 0;
        else
            step++;

        MQTTManager.getManager().publishMessage( MainActivity.topicLocation + topic + "/step", String.valueOf(step));

        notifyPropertyChanged(BR.step);
        notifyPropertyChanged(BR.text);
    }

    //region getters

    public int maxStep(){
        return this.views.size();
    }

    @Bindable
    public boolean isClickable() {
        return clickable;
    }

    @Bindable
    public int getText(){
        return this.views.get(this.step).getText();
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

    public static Fairytale[] fairytales = new Fairytale[]{
            new FairytaleTheePigs(),
            new Fairytale("Hansel And Gretel", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground, "HanselAndGretel"),
            new Fairytale("Cinderella", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground, "Cinderella")
    };

    private void setClickable(boolean clickable) {
        this.clickable = clickable;
        notifyPropertyChanged(BR.clickable);
    }

    public void MessageReceived(MqttMessage message) {
        Log.d("TAG", "MessageReceived: " + message);
        if(message.toString().equals("0")){
            setClickable(true);
        }else if(message.toString().equals("1")){
            setClickable(false);
        }
        if(message.toString().startsWith("feedback: ")){
            feedback = message.toString().replace("feedback: ", "");
            notifyPropertyChanged(BR.feedback);
        }

    }


}
