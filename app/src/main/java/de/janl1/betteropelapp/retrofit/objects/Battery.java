package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

public class Battery {

    @SerializedName("range")
    public double range;

    @SerializedName("level")
    public double level;

    @SerializedName("timestamp")
    public double timestamp;
}
