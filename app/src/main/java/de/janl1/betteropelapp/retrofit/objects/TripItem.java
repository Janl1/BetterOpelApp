package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

public class TripItem {

    @SerializedName("odometer")
    public double odometer;

    @SerializedName("level")
    public double level;

    @SerializedName("speed")
    public double speed;

    @SerializedName("range")
    public double range;

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("elevation")
    public double elevation;

    @SerializedName("createdAt")
    public String createdAt;
}
