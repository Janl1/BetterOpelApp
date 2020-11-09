package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("timestamp")
    public double timestamp;
}
