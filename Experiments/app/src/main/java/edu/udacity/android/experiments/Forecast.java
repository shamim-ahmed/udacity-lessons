package edu.udacity.android.experiments;

public class Forecast {
    private String day;
    private String description;
    private double minimumTemparature;
    private double maximumTemparature;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMinimumTemparature() {
        return minimumTemparature;
    }

    public void setMinimumTemparature(double minimumTemparature) {
        this.minimumTemparature = minimumTemparature;
    }

    public double getMaximumTemparature() {
        return maximumTemparature;
    }

    public void setMaximumTemparature(double maximumTemparature) {
        this.maximumTemparature = maximumTemparature;
    }

    @Override
    public String toString() {
        return day + " - " + description + " " + minimumTemparature + "/" + maximumTemparature;
    }
}
