package com.example.mobapp;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.zip.Inflater;

public class Fairytale extends BaseObservable implements Serializable {

    private final String topic;
    private final String name;
    private final String location;
    private final String timeToComplete;
    private final String description;
    private final int image;
    private int step;

    public class Step extends Fragment {
        private final int layout;

        public Step(int layout) {
            this.layout = layout;
        }

        public int getLayout() {
            return layout;
        }
    }

    protected HashMap<Integer, Step> steps;

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
        this.step = 1;
        this.topic = topic;
        this.color = R.color.transparent;
        this.clickable = true;
    }

    private View currentLayout;

    @Bindable
    public View getLayout(){
       return this.currentLayout;
    }

    public void setLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.currentLayout = inflater.inflate(this.steps.get(this.step).getLayout(), null);

        ConstraintLayout layout = ((Activity) context).findViewById(R.id.inspector_constraintLayout);
        ConstraintSet set = new ConstraintSet();

        layout.addView(this.currentLayout, 0);

        set.clone(layout);
        set.connect(this.currentLayout.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
        set.connect(this.currentLayout.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        set.applyTo(layout);



        notifyPropertyChanged(BR.layout);
    }


    public void nextStep(Context context){
        setLayout(context);

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
      new FairytaleTheePigs(),
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground, "HanselAndGretel"),
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground, "Cinderella")
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
        if(message.toString().equals("true")){
            setClickable(true);
        }else if(message.toString().equals("false")){
            setClickable(false);
        }
    }



}
