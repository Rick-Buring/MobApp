package com.example.mobapp;

public class Fairytale {

    private final String name;
    private final String location;
    private final String timeToComplete;
    private final String description;
    private final int image;

    public Fairytale(String name, String location, String timeToComplete, String description, int image) {
        this.name = name;
        this.location = location;
        this.timeToComplete = timeToComplete;
        this.description = description;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getTimeToComplete() {
        return timeToComplete;
    }

    public String getDescription() {
        return description;
    }
}
