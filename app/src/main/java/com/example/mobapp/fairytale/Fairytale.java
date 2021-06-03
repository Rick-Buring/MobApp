package com.example.mobapp.fairytale;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.mobapp.BR;
import com.example.mobapp.R;

import java.io.Serializable;

public class Fairytale extends BaseObservable implements Serializable {

    private final String name;
    private final String location;
    private final String timeToComplete;
    private final String description;
    private final int image;
    private int step;

    public Fairytale(String name, String location, String timeToComplete, String description, int image) {
        this.name = name;
        this.location = location;
        this.timeToComplete = timeToComplete;
        this.description = description;
        this.image = image;
        this.step = 1;
    }

    public void nextStep(){
        step++;
        //todo update variable

        notifyPropertyChanged(BR.step);
    }

    @Bindable
    public int getStep() {
        return this.step;
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
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground),
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground),
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground),
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground),
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground),
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground),
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground),
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground),
      new Fairytale("Test", "Thuis", "4 uur", "dit werkt nu in een keer", R.drawable.ic_launcher_foreground)
    };

}
