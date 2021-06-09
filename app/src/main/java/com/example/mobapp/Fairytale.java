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
    private boolean clickable;
    private String feedback = "";
    public ArrayList<fairytaleStepView> views;

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

        MQTTManager.getManager().publishMessage(topic + "/step", String.valueOf(step));

        notifyPropertyChanged(BR.stepString);
        notifyPropertyChanged(BR.text);
    }

    //region getters

    @Bindable
    public boolean isClickable() {
        return clickable;
    }

    @Bindable
    public int getText(){
        return this.views.get(this.step).getText();
    }

    @Bindable
    public String getStepString() {
        //todo make a not hardcoded string
        return "stap: " + (step + 1);
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
    public String getFeedback() {
        return feedback;
    }

    public String getTopic() {
        return this.topic;
    }

    //endregion

    public static Fairytale[] fairytales = new Fairytale[]{
            new FairytaleTheePigs(),
//            new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground, "HanselAndGretel"),
//            new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground, "Cinderella")
    };

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
        if(message.toString().startsWith("feedback: ")){
            feedback = message.toString().replace("feedback: ", "");
            notifyPropertyChanged(BR.feedback);
        }

    }


}
