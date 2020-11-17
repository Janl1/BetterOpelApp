package de.janl1.betteropelapp.retrofit.objects;

import com.google.gson.annotations.SerializedName;

public class Consumption {

    @SerializedName("distance")
    public double distance;

    @SerializedName("consumption")
    public double consumption;
}
