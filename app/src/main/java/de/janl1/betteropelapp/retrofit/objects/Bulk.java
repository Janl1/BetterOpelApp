package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

public class Bulk {

    @SerializedName("vin")
    public String vin;

    @SerializedName("odometer")
    public double odometer;

    @SerializedName("range")
    public double range;

    @SerializedName("level")
    public double level;

    @SerializedName("charging")
    public String charging;

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("timestamp")
    public double timestamp;
}
